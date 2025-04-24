package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findById(long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public int findTotalQuantityById(long couponId) {
        return couponJpaRepository.findTotalQuantityById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
    @Override
    public Optional<Coupon> findByIdWithLock(long couponId) {
        return couponJpaRepository.findByIdWithLock(couponId);
    }
}
