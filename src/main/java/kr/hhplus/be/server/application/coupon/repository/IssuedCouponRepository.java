package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;

public interface IssuedCouponRepository {

    IssuedCoupon findById(Long couponId);

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    int countByCouponId(Long couponId);
}
