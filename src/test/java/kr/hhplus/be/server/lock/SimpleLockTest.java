package kr.hhplus.be.server.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

public class SimpleLockTest {
    public static void main(String[] args) {
        // 1. Redisson 설정
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379"); // 도커로 띄운 Redis 연결
        RedissonClient redisson = Redisson.create(config);

        // 2. 락 객체 가져오기
        RLock lock = redisson.getLock("my-test-lock");

        try {
            // 3. 락 시도 (최대 3초 동안 기다리고, 10초 동안 락 유지)
            boolean available = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (available) {
                System.out.println("✅ 락 획득 성공! Critical Section 진입");

                // 중요 작업 하는 부분
                Thread.sleep(5000); // 5초 동안 작업하는 척

                System.out.println("✅ 작업 완료");
            } else {
                System.out.println("❌ 락 획득 실패! 다른 누군가가 락을 잡고 있음");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 4. 락 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("✅ 락 해제 완료");
            }
        }

        // 5. 레디슨 종료
        redisson.shutdown();
    }
}
