package kr.hhplus.be.server.application.coupon.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.infrastructure.redis.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String COUPON_STOCK_KEY = "coupon:%d:stock";
    private static final String COUPON_ISSUED_KEY = "coupon:%d:issued";

    public IssuedCoupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return issuedCouponRepository.findById(couponId);
    }

    @DistributedLock(key = "T(java.lang.String).format('Coupon%d', #couponId)")
    public void issueCoupon(long userId, long couponId) {

            // 1. 이미 발급받았는지 확인
            if (issuedCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
            }

            // 2. 발급 수량 초과 확인
            Coupon coupon = //couponRepository.findById(couponId)
                    couponRepository.findByIdWithLock(couponId)
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

            // 3. 발급 여부 확인 , 재고 차감
               coupon.isIssueAvailable(coupon.getTotalQuantity());

           // 4. 발급
            IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                                    .userId(userId)
                                    .coupon(coupon)
                                    .issuedAt(LocalDateTime.now())
                                    .build();
            // 5. 발급된 쿠폰 저장
            issuedCouponRepository.save(issuedCoupon);

            // 6. 쿠폰 재고 차감 저장
            couponRepository.save(coupon);
    }

    // 쿠폰 초기화
    @Transactional
    public void initializeCouponStockRedis(long couponId, int totalQuantity) {
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);

        // Redis에 초기 재고 설정
        for (int i = 0; i < totalQuantity; i++) {
            redisTemplate.opsForList().rightPush(stockKey, String.valueOf(i + 1));
        }

        // 만료 시간 설정 (예: 24시간)
        redisTemplate.expire(stockKey, 24, TimeUnit.HOURS);
    }

    // 쿠폰 발급
    @Transactional
    public void issueCouponRedis(long userId, long couponId) {
        String stockKey = String.format(COUPON_STOCK_KEY, couponId);
        String issuedKey = String.format(COUPON_ISSUED_KEY, couponId);

        // 중복 발급 체크
        Boolean isIssued = redisTemplate.opsForSet().isMember(issuedKey, String.valueOf(userId));
        if (Boolean.TRUE.equals(isIssued)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        // 재고 감소
        String couponNumber = redisTemplate.opsForList().leftPop(stockKey);
        if (couponNumber == null) {
            throw new IllegalStateException("쿠폰이 소진되었습니다.");
        }

        // 쿠폰 발급 처리
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        IssuedCoupon issuedCoupon = IssuedCoupon.builder()
                .userId(userId)
                .coupon(coupon)
                .issuedAt(LocalDateTime.now())
                .build();

        issuedCouponRepository.save(issuedCoupon);

        // 발급 내역 저장
        redisTemplate.opsForSet().add(issuedKey, String.valueOf(userId));

        return true;
    }




}
