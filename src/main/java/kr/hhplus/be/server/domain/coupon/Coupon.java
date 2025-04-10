package kr.hhplus.be.server.domain.coupon;

public class Coupon {


    private long couponId;
    private CouponPolicy couponPolicy;
    private boolean isUsed;
    private boolean isExpired;

    public Coupon(Long id, CouponPolicy couponPolicy, boolean isUsed, boolean isExpired) {
        this.couponId = id;
        this.couponPolicy = couponPolicy;
        this.isUsed = isUsed;
        this.isExpired = isExpired;
    }

    public long calculateDiscount(long totalAmount) {
        return couponPolicy.applyDiscount(totalAmount);
    }

    public boolean isValid() {
        //만기 여부, 사용 여부 체크
        if (isExpired || isUsed) {
            return false;
        }
        return true;
    }
}