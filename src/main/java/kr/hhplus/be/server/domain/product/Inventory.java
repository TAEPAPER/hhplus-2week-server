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

    @Column(nullable = false)
    private int stock;

    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    public Inventory(int stock) {
        this.stock = stock;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasEnough(int quantity) {
        return stock >= quantity;
    }

    public void deduct(int quantity) {
        this.stock = stock - quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void addStock(int quantity) {
        this.stock += quantity;
        this.updatedAt = LocalDateTime.now();
    }
}
