package kr.hhplus.be.server.domain.product;


import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(length = 50, nullable = false)
    private  String name;

    @Column(length = 50, nullable = false)
    private  long price;

    @OneToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private  Inventory inventory;

    @Column(length = 200, nullable = false)
    private String description;

    public long getId() { return id; }
    public long getPrice() { return price; }
    public String getName() { return name;}
    public String getDescription() { return description;}

    public void isStockAvailable(int quantity) {
        if (!inventory.hasEnough(quantity)) {
            throw new IllegalStateException("상품 재고 부족");
        }
    }

    public void deduct(int quantity) {
        if (!inventory.hasEnough(quantity)) {
            throw new IllegalStateException("재고 부족으로 차감 불가");
        }
        this.inventory.deduct(quantity);
    }
}
