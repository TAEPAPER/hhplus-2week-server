package kr.hhplus.be.server.application.coupon.service;


import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.user.repository.UserRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public IssuedCoupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return issuedCouponRepository.findById(couponId);
    }

    @Transactional
    public void issueCoupon(long userId, long couponId) {
        // 1. 유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

            // 2. 이미 발급받았는지 확인
            if (issuedCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
            }

            // 3. 발급 수량 초과 확인
            Coupon coupon = //couponRepository.findById(couponId)
                    couponRepository.findByIdWithLock(couponId)
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

            //현재 남은 수량
            int issuedCount = couponRepository.findTotalQuantityById(couponId);
            //발급 여부 확인 , 재고 차감
           coupon.isIssueAvailable(issuedCount);

            // 4. 발급
            IssuedCoupon issuedCoupon = IssuedCoupon.builder().user(user)
                                    .coupon(coupon)
                                    .issuedAt(LocalDateTime.now())
                                    .build();

            issuedCouponRepository.save(issuedCoupon);
            couponRepository.save(coupon);


    }
}
