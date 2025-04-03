package kr.hhplus.be.server.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IssuedCouponResponse {
    private Long issuedCouponId;
    private Long couponId;
    private BigDecimal discountAmount;
    private boolean isUsed;
    private LocalDateTime expiredAt;

    public IssuedCouponResponse(long issuedCouponId, long couponId, BigDecimal discountAmount, boolean isUsed, LocalDateTime expiredAt) {
        this.issuedCouponId = issuedCouponId;
        this.couponId = couponId;
        this.discountAmount = discountAmount;
        this.isUsed = isUsed;
        this.expiredAt = expiredAt;
    }

}