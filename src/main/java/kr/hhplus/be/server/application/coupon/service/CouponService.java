package kr.hhplus.be.server.application.coupon.service;


import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final List<IssuedCoupon> issuedCoupons = new ArrayList<>();
    private final int maxCoupons = 5;
    private final ReentrantLock lock = new ReentrantLock();


    public IssuedCoupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return issuedCouponRepository.findById(couponId);
    }


}
