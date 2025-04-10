package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;

public interface CouponRepository {
    IssuedCoupon findById(Long couponId);
}
