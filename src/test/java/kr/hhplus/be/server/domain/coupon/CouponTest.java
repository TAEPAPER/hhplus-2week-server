package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void isIssueAvailable_Success() {
        // Given
        CouponPolicy couponPolicy = new FixedAmountDiscountPolicy();
        Coupon coupon = Coupon.builder()
                                .id(1L)
                                .name("Test Coupon")
                                .discountAmount(1000)
                                .totalQuantity(5)
                                .validUnit("days")
                                .validValue(30)
                                .type("FIXED")
                                .build();

        // When & Then
        assertDoesNotThrow(() -> coupon.isIssueAvailable(5));
    }

    @Test
    void isIssueAvailable_ExceedsQuantity() {
        // Given
        CouponPolicy couponPolicy = new FixedAmountDiscountPolicy();
        Coupon coupon = Coupon.builder()
                                .id(1L)
                                .name("Test Coupon")
                                .discountAmount(1000)
                                .totalQuantity(5)
                                .validUnit("days")
                                .validValue(30)
                                .type("FIXED")
                                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> coupon.isIssueAvailable(5));
        assertEquals("쿠폰 발급 수량이 초과되었습니다.", exception.getMessage());
    }
}