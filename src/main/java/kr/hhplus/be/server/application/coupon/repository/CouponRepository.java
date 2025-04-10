package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.Coupon;

import java.util.Optional;

public interface CouponRepository {

    boolean existsByUserIdAndCouponId(long userId, long couponId);

    Optional<Coupon> findById(long couponId);

    int countByCouponId(long couponId);
}
