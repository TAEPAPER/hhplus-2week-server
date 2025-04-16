package kr.hhplus.be.server.domain.point;


import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Entity
public class Point {

    @Id
    private Long userId;

    @Getter
    private long balance;

    @Getter
    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Point(Long userId, User user, long balance) {
        this.userId = userId;
        this.user = user;
        this.balance = balance;
    }
    public Point() {
    }

    //포인트를 충전한다
    public Point charge(long amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        long totalAmount = this.balance + amount;

        return Point.builder()
                .userId(this.userId)
                .user(this.user)
                .balance(totalAmount)
                .build();
    }

    //포인트를 사용한다.
    public Point use(long paymentAmount) {
        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        long totalAmount = this.balance - paymentAmount;

        if (totalAmount < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        return Point.builder()
                .userId(this.userId)
                .user(this.user)
                .balance(totalAmount)
                .build();
    }
}
