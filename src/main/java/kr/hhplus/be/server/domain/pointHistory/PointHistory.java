package kr.hhplus.be.server.domain.pointHistory;

import jakarta.persistence.Entity;
import kr.hhplus.be.server.application.common.ClockHolder;
import kr.hhplus.be.server.domain.point.Point;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false)
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private TransactionType type;

    @Column(precision = 10, scale = 2, nullable = false)
    private long amount;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public PointHistory(Point point, long amount, TransactionType type) {
        this.point = point;
        this.amount = amount;
        this.type = type;
    }

    public static PointHistory createChargeHistory(Point point, long amount) {
        return new PointHistory(point, amount, TransactionType.CHARGE);
    }

    public static PointHistory createUseHistory(Point point, long amount) {
        return new PointHistory(point, amount, TransactionType.USE);
    }

}
