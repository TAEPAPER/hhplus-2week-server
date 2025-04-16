package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(nullable = false)
    private long totalPrice;

    @OneToOne
    @JoinColumn(name = "issued_coupon_id")
    private IssuedCoupon coupon;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Order( User user, List<OrderItem> items, OrderStatus orderStatus, long totalPrice) {
        this.user = user;
        this.orderStatus = orderStatus;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }


    public List<OrderItem> getOrderItems() {
        return items;
    }

    public static Order create(User user, List<ProductQuantity> productQuantities, IssuedCoupon coupon) {

        List<OrderItem> orderItems = new ArrayList<>();
        long totalPrice = 0;

        for (ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();

            // 재고 확인
            product.isStockAvailable(quantity);

            long unitPrice = product.getPrice() * quantity;
            totalPrice += unitPrice;

            //재고 차감
            product.deduct(quantity);

            orderItems.add(new OrderItem(product, quantity, unitPrice));
        }

        // 쿠폰 적용
        if (coupon.isValid()) {
            totalPrice -= coupon.calculateDiscount(totalPrice);
            totalPrice = Math.max(totalPrice, 0);
        }

        return new Order(user, orderItems, OrderStatus.CREATED ,totalPrice);
    }

    public record ProductQuantity(Product product, int quantity) {}

}

