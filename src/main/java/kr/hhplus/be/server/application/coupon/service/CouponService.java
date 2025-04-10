package kr.hhplus.be.server.application.coupon.service;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    // 쿠폰 ID별 Lock 보관소
    private final Map<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public IssuedCoupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return issuedCouponRepository.findById(couponId);
    }

    public void issueCoupon(long userId, long couponId) {
        // 쿠폰별 락 가져오기
        ReentrantLock lock = lockMap.computeIfAbsent(couponId, id -> new ReentrantLock());



    }
}
