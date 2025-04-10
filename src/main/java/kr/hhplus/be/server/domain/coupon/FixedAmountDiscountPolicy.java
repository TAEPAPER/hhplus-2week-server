package kr.hhplus.be.server.domain.coupon;

public class FixedAmountDiscountPolicy implements CouponPolicy {
    private final long discountAmount;

    public FixedAmountDiscountPolicy(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public long applyDiscount(long totalAmount) {
        return totalAmount - discountAmount;
    }
}