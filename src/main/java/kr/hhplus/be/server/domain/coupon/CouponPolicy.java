package kr.hhplus.be.server.domain.coupon;

public interface CouponPolicy {
    long applyDiscount(long totalAmount);
}

class PercentageDiscountPolicy implements CouponPolicy {
    private final long percentage;

    public PercentageDiscountPolicy(long percentage) {
        this.percentage = percentage;
    }

    @Override
    public long applyDiscount(long totalAmount) {
        return totalAmount - (totalAmount * percentage / 100);
    }
}

class FixedAmountDiscountPolicy implements CouponPolicy {
    private final long discountAmount;

    public FixedAmountDiscountPolicy(long discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public long applyDiscount(long totalAmount) {
        return totalAmount - discountAmount;
    }
}