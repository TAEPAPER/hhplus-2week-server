package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @CreationTimestamp
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public IssuedCoupon(Long userId, Coupon coupon, LocalDateTime issuedAt) {
        this.userId = userId;
        this.coupon = coupon;
        this.issuedAt = issuedAt;

        // 쿠폰 만료일 계산
        if (coupon.getValidUnit().equals("days")) {
            this.expiredAt = issuedAt.plusDays(coupon.getValidValue());
        } else if (coupon.getValidUnit().equals("months")) {
            this.expiredAt = issuedAt.plusMonths(coupon.getValidValue());
        } else if (coupon.getValidUnit().equals("years")) {
            this.expiredAt = issuedAt.plusYears(coupon.getValidValue());
        } else {
            throw new IllegalArgumentException("Invalid valid unit: " + coupon.getValidUnit());
        }


        this.isUsed = false;
    }

    public long calculateDiscount(long totalAmount) {
        return coupon.applyDiscount(totalAmount);
    }

    public boolean isValid() {
        return !isUsed && expiredAt.isAfter(LocalDateTime.now());
    }

    public void markAsUsed() {
        this.isUsed = true;
    }
}
