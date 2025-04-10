package kr.hhplus.be.server.application.coupon.service;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    
    public Coupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return couponRepository.findById(couponId);
    }

}
