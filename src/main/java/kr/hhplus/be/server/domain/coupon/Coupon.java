package kr.hhplus.be.server.domain.coupon;



public class Coupon {

    private final Long couponId;
    private final int discountAmount;
    private final int totalQuantity;
    private final CouponPolicy couponPolicy;

    public Coupon(Long couponId, String name, int discountAmount, int totalQuantity, CouponPolicy couponPolicy) {
        this.couponId = couponId;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.couponPolicy = couponPolicy;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public CouponPolicy getCouponPolicy() {
        return couponPolicy;
    }

    public void isIssueAvailable(int issuedCount) {
        if(totalQuantity <= issuedCount) {
            throw new IllegalArgumentException("쿠폰 발급 수량이 초과되었습니다.");
        }
    }
}

