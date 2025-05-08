package kr.hhplus.be.server.domain.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private long price;

    @OneToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(length = 200, nullable = false)
    private String description;

    // 생성자 추가
    public Product(String name, long price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // 재고 관련 메서드는 유지하되 명확한 이름으로 변경
    public void checkStockAvailability(int quantity) {
        if (inventory == null || !inventory.hasEnough(quantity)) {
            throw new IllegalStateException("상품 재고 부족");
        }
    }

    public void deductStock(int quantity) {
        if (inventory == null || !inventory.hasEnough(quantity)) {
            throw new IllegalStateException("재고 부족으로 차감 불가");
        }
        this.inventory.deduct(quantity);
    }


}
