package kr.hhplus.be.server.domain.coupon;

public class PercentageDiscountPolicy implements CouponPolicy {
    private final long percentage;

    public PercentageDiscountPolicy(long percentage) {
        this.percentage = percentage;
    }

    @Override
    public long applyDiscount(long totalAmount) {
        return totalAmount - (totalAmount * percentage / 100);
    }
}