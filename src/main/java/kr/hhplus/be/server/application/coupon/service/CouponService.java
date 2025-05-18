package kr.hhplus.be.server.application.coupon.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.infrastructure.redis.lock.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;

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
}
