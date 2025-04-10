package kr.hhplus.be.server.domain.coupon;

public class NoCoupon extends Coupon {

    public NoCoupon() {
        super(0L, null, false, false);
    }

    @Override
    public long calculateDiscount(long totalAmount) {
        return 0; // 할인 없음
    }

    @Override
    public boolean isValid() {
        return true; // 항상 유효한 것으로 처리
    }
}