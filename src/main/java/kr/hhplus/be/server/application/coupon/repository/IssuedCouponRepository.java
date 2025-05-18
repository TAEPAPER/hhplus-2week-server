package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;

import java.util.List;

public interface IssuedCouponRepository {

    IssuedCoupon findById(Long couponId);

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    int countByCouponId(Long couponId);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    boolean existsByUserIdAndCouponIdWithLock(long userId, long couponId);

    void saveAll(List<IssuedCoupon> newIssuedCoupons);

    List<IssuedCoupon> findAll();
}
