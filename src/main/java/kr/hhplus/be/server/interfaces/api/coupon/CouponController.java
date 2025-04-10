package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    @PostMapping("/issue")
    public ResponseEntity<Void> issueCoupon(@RequestBody CouponIssueRequest request) {
        //couponFacade.issueCoupon(request);
        return ResponseEntity.ok().build();
    }

}
