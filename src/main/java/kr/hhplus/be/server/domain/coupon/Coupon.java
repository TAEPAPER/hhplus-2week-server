package kr.hhplus.be.server.domain.coupon;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {

    @Id
    private Long id;

    @Column
    private String name;

    @Column(name="discount_amount")
    private int discountAmount;

    @Column(name="total_quantity")
    private int totalQuantity;

    @Column(name="valid_value")
    private int validValue;

    @Column(name="valid_unit")
    private String validUnit;

    @Column
    private String type;

    @Transient
    private  CouponPolicy couponPolicy;

    @PostLoad
    private void postLoad() {
        this.couponPolicy = CouponPolicyResolver.resolve(this.type);
    }

    @Builder
    public Coupon(Long id, String name, int discountAmount, int totalQuantity, int validValue, String validUnit, String type) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.validValue = validValue;
        this.validUnit = validUnit;
        this.type = type;
    }


    public long applyDiscount(long totalAmount) {
        if (couponPolicy == null) {
            throw new IllegalStateException("CouponPolicy 미주입 상태입니다.");
        }
        return couponPolicy.applyDiscount(totalAmount, this.discountAmount);
    }

    public void isIssueAvailable(int issuedCount) {
        if(totalQuantity <= issuedCount) {
            throw new IllegalArgumentException("쿠폰 발급 수량이 초과되었습니다.");
        }
    }
}

