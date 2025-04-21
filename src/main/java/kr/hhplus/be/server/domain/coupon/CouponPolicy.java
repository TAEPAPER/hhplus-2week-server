package kr.hhplus.be.server.domain.coupon;

public interface CouponPolicy {

    long applyDiscount(long totalAmount, int discountAmount);

}
