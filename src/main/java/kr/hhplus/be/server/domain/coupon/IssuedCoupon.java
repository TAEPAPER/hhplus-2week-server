package kr.hhplus.be.server.domain.coupon;

public class IssuedCoupon {


    private long couponId;
    private long userId;
    private CouponPolicy couponPolicy;
    private boolean isUsed;
    private boolean isExpired;

    public IssuedCoupon(Long userId, Long couponId, CouponPolicy couponPolicy, boolean isUsed, boolean isExpired) {
        this.userId = userId;
        this.couponId = couponId;
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