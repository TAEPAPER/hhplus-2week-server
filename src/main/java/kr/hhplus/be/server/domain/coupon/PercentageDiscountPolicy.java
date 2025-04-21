package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Component;

@Component("PERCENTAGE")
public class PercentageDiscountPolicy implements CouponPolicy {

    @Override
    public long applyDiscount(long totalAmount, int percentage) {
        return totalAmount - (totalAmount * percentage / 100);
    }
}