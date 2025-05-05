package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

    int countByCouponId(Long couponId);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(i) > 0 FROM IssuedCoupon i WHERE i.userId = :userId AND i.coupon.id = :couponId")
    boolean existsByUserIdAndCouponIdWithLock(long userId, long couponId);

}