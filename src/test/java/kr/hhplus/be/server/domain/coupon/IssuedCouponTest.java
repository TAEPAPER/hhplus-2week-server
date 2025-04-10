package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IssuedCouponTest {

    @Mock
    private CouponPolicy couponPolicy;

    @Test
    void calculateDiscount_정상작동() {
        // given
        MockitoAnnotations.openMocks(this);
        long totalAmount = 10000L;
        long discountAmount = 2000L;
        when(couponPolicy.applyDiscount(totalAmount)).thenReturn(discountAmount);

        IssuedCoupon coupon = new IssuedCoupon(1L, couponPolicy, false, false);

        // when
        long result = coupon.calculateDiscount(totalAmount);

        // then
        assertThat(result).isEqualTo(discountAmount);
        verify(couponPolicy, times(1)).applyDiscount(totalAmount);
    }

    @Test
    void isValid_만료_또는_사용된_쿠폰() {
        // given
        IssuedCoupon expiredCoupon = new IssuedCoupon(1L, couponPolicy, false, true);
        IssuedCoupon usedCoupon = new IssuedCoupon(2L, couponPolicy, true, false);

        // when
        boolean isExpiredCouponValid = expiredCoupon.isValid();
        boolean isUsedCouponValid = usedCoupon.isValid();

        // then
        assertThat(isExpiredCouponValid).isFalse();
        assertThat(isUsedCouponValid).isFalse();
    }

    @Test
    void isValid_유효한_쿠폰() {
        // given
        IssuedCoupon validCoupon = new IssuedCoupon(1L, couponPolicy, false, false);

        // when
        boolean isValid = validCoupon.isValid();

        // then
        assertThat(isValid).isTrue();
    }
}