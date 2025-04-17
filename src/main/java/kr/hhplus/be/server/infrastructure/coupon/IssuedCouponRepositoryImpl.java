package kr.hhplus.be.server.infrastructure.coupon;


import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final IssuedCouponJpaRepository issuedCouponRepository;

    @Override
    public IssuedCoupon findById(Long couponId) {
        return issuedCouponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰 정보가 없습니다!"));
    }

    @Override
    public IssuedCoupon save(IssuedCoupon issuedCoupon) {
        return issuedCouponRepository.save(issuedCoupon);
    }

    @Override
    public int countByCouponId(Long couponId) {
        return issuedCouponRepository.countByCouponId(couponId);
    }


}
