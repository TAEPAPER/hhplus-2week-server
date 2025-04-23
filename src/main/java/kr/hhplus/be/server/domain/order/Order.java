package kr.hhplus.be.server.domain.order;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    // 기본 생성자 추가
    protected Order() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(nullable = false)
    private long totalPrice;

    @Column(name="issued_coupon_id")
    @Nullable
    private Long issuedCouponId;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Order( User user, List<OrderItem> items, OrderStatus orderStatus, long totalPrice, @Nullable Long issuedCouponId) {
        this.user = user;
        this.orderStatus = orderStatus;
        this.items = items;
        this.totalPrice = totalPrice;
        this.issuedCouponId = issuedCouponId;
    }

    public static Order create(User user, List<ProductQuantity> productQuantities, IssuedCoupon coupon) {

        List<OrderItem> orderItems = new ArrayList<>();

        long totalPrice = 0;

        for (ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();

            // 재고 확인
            product.checkStockAvailability(quantity);

            long unitPrice = product.getPrice() * quantity;
            totalPrice += unitPrice;

            orderItems.add(new OrderItem(null,product, quantity, unitPrice));
        }

        // 쿠폰 적용
        if (coupon.isValid()) {
            totalPrice  = coupon.calculateDiscount(totalPrice);
            coupon.markAsUsed();
            totalPrice = Math.max(totalPrice, 0);
        }

       Order order = new Order(user, orderItems, OrderStatus.CREATED ,totalPrice, coupon.getId());
        for(OrderItem item : orderItems) {
            item.setOrder(order);
        }
        return order;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public record ProductQuantity(Product product, int quantity) {}


}
