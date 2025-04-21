package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품 연관관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int stock;

    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    public Inventory(Product product, int stock) {
        this.product = product;
        this.stock = stock;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasEnough(int quantity) {
        return stock >= quantity;
    }

    public void deduct(int quantity) {
        if (!hasEnough(quantity)) {
            throw new IllegalStateException("재고 부족으로 차감 불가");
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void addStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }


}
