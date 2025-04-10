package kr.hhplus.be.server.application.coupon.service;


import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    
    public Coupon getById(long couponId) {
       return couponRepository.findById(couponId);
    }

}
