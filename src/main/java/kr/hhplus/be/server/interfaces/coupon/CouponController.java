package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.interfaces.coupon.dto.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @PostMapping("/issue")
    public ResponseEntity<Void> issueCoupon(@RequestBody CouponIssueRequest request) {
        try {
            couponService.issueCouponRedis(request.userId(), request.couponId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 쿠폰 초기화
    @PostMapping("/{couponId}/initialize")
    public ResponseEntity<Void> initializeCouponStock(@PathVariable long couponId, @RequestParam int totalQuantity) {
        couponService.initializeCouponStockRedis(couponId, totalQuantity);
        return ResponseEntity.ok().build();
    }

}
