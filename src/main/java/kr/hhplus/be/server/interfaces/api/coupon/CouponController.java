package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<Void> issueCoupon(@RequestBody CouponIssueRequest request) {
        couponService.issueCoupon(request.userId(), request.couponId());
        return ResponseEntity.ok().build();
    }

}
