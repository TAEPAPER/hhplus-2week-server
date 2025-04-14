package kr.hhplus.be.server.interfaces.api.coupon.dto;

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

    public Long getIssuedCouponId() {
        return issuedCouponId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}