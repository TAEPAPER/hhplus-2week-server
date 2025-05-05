package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class IssuedCouponTest {

    @Mock
    private CouponPolicy couponPolicy;

    @Test
    void calculateDiscount_정상작동() {
        // given
        User user = User.builder().id(1L).name("Test User").build();
        Coupon coupon = Coupon.builder().id(1L).name("Test Coupon").discountAmount(2000).totalQuantity(100).validUnit("days").validValue(30).type("FIXED").build();

        MockitoAnnotations.openMocks(this);

        IssuedCoupon issuedCoupon = new IssuedCoupon(1L, coupon, LocalDateTime.now());
        long expected = 8000L;

        //when
        long actual = issuedCoupon.calculateDiscount(10000);


        //then
        assertThat(actual).isEqualTo(expected);


    }

    @Test
    void isValid_만료_또는_사용된_쿠폰() {
       /* // given
        User user = User.builder().id(1L).name("Test User").build();
        Coupon coupon = Coupon.builder().id(1L).name("Test Coupon").discountAmount(2000).totalQuantity(100).validUnit("days").validValue(30).type("FIXED").build();


        IssuedCoupon expiredCoupon = new IssuedCoupon(1L, 1L, couponPolicy, false, true);
        IssuedCoupon usedCoupon = new IssuedCoupon(2L, 1L, couponPolicy, true, false);

        // when
        boolean isExpiredCouponValid = expiredCoupon.isValid();
        boolean isUsedCouponValid = usedCoupon.isValid();

        // then
        assertThat(isExpiredCouponValid).isFalse();
        assertThat(isUsedCouponValid).isFalse();*/
    }

    @Test
    void isValid_유효한_쿠폰() {
       /* // given
        IssuedCoupon validCoupon = new IssuedCoupon(1L, 1L, couponPolicy, false, false);

        // when
        boolean isValid = validCoupon.isValid();

        // then
        assertThat(isValid).isTrue();*/
    }
}