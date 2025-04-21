package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CouponRepository {

    boolean existsById(long userId, long couponId);

    Optional<Coupon> findById(long couponId);

    int countByCouponId(long couponId);
}
