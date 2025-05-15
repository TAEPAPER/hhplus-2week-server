package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
public class TopSellingProduct implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Column(name="product_id", nullable = false)
    private Product product;

    @Column(name="total_sold", nullable = false)
    private int totalSold;

    @Column(name="aggregated_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate aggregatedAt;

    @Builder
    public TopSellingProduct(Product product, int totalSold, LocalDate aggregatedAt) {
        this.product = product;
        this.totalSold = totalSold;
        this.aggregatedAt = aggregatedAt;
    }

    public TopSellingProduct() {

    }

    // 비즈니스 메서드로 판매량 증가 처리
    public TopSellingProduct increaseTotalSold(int additionalSold) {
        return TopSellingProduct.builder()
                .product(this.product)
                .totalSold(this.totalSold + additionalSold)
                .aggregatedAt(this.aggregatedAt)
                .build();
    }
}
