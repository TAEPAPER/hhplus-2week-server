package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Component;

@Component("FIXED")
public class FixedAmountDiscountPolicy implements CouponPolicy {

    @Override
    public long applyDiscount(long totalAmount, int discountAmount) {
        return totalAmount - discountAmount;
    }

}