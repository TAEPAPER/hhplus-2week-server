package kr.hhplus.be.server.application.coupon.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CouponRepository {


    Optional<Coupon> findById(long couponId);

    int findTotalQuantityById(long couponId);

    Coupon save(Coupon coupon);

    Optional<Coupon> findByIdWithLock( long couponId);

}
