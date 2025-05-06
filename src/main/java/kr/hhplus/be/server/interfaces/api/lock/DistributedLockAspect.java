package kr.hhplus.be.server.interfaces.api.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 트랜잭션보다 먼저 실행
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(kr.hhplus.be.server.interfaces.api.lock.DistributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX +
                CustomSpringELParser.getDynamicValue(
                        signature.getParameterNames(),
                        joinPoint.getArgs(),
                        distributedLock.key());
        log.info("lock on [method:{}] [key:{}]", method, key);

        RLock rLock = redissonClient.getLock(key);
        String lockName = rLock.getName();
        try {
            boolean available =
                    rLock.tryLock(
                            distributedLock.waitTime(),
                            distributedLock.leaseTime(),
                            distributedLock.timeUnit());
            if (!available) {
                throw new IllegalStateException("Lock failed");
            }

            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            //락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
            return null;
        } finally {
            try {
                rLock.unlock();
                log.info("unlock complete [Lock:{}] ", lockName);
            } catch (IllegalMonitorStateException e) {//락이 이미 종료되었을때 발생
                log.info("Redisson Lock Already Unlocked");
            }
        }


    }

}
