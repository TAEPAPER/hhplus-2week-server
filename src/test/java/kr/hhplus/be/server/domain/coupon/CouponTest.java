package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void isIssueAvailable_Success() {
        // Given
        CouponPolicy couponPolicy = new FixedAmountDiscountPolicy(1000);
        Coupon coupon = new Coupon(1L, "Test Coupon", 1000, 10, couponPolicy);

        // When & Then
        assertDoesNotThrow(() -> coupon.isIssueAvailable(5));
    }

    @Test
    void isIssueAvailable_ExceedsQuantity() {
        // Given
        CouponPolicy couponPolicy = new FixedAmountDiscountPolicy(1000);
        Coupon coupon = new Coupon(1L, "Test Coupon", 1000, 5, couponPolicy);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> coupon.isIssueAvailable(5));
        assertEquals("쿠폰 발급 수량이 초과되었습니다.", exception.getMessage());
    }
}