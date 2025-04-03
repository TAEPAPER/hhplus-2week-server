package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.response.IssuedCouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/coupons")
@Tag(name = "쿠폰", description = "선착순 쿠폰 API")
public class CouponController {

    private final Map<Long, List<IssuedCouponResponse>> userCoupons = new ConcurrentHashMap<>();
    private final AtomicLong issuedIdGen = new AtomicLong(1);
    private final int maxCoupons = 5;

    @Operation(summary = "선착순 쿠폰 발급")
    @PostMapping("/issue/{userId}")
    public ResponseEntity<String> issue(
            @Parameter(description = "쿠폰을 발급받을 사용자 ID")
            @PathVariable("userId") Long userId) {
        List<IssuedCouponResponse> all = userCoupons.values().stream().flatMap(List::stream).toList();
        if (all.size() >= maxCoupons) return ResponseEntity.status(429).body("쿠폰 소진");

        IssuedCouponResponse coupon = new IssuedCouponResponse(
            issuedIdGen.getAndIncrement(), 1L, new BigDecimal("1000"), false, LocalDateTime.now().plusDays(1));
        userCoupons.computeIfAbsent(userId, k -> new ArrayList<>()).add(coupon);
        return ResponseEntity.ok("쿠폰 발급 완료");
    }

    @Operation(summary = "내 쿠폰 목록 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<List<IssuedCouponResponse>> myCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(userCoupons.getOrDefault(userId, List.of()));
    }
}
