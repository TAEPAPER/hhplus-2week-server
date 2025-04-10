package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.product.Product;

import java.util.ArrayList;
import java.util.List;


public class Order {

    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private List<OrderItem> items;
    private long totalPrice;

    public Order( long userId, List<OrderItem> items, OrderStatus orderStatus, long totalPrice) {
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public long getUserId() {
        return userId;
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


    public long getOrderId() {
        return orderId;
    }


    public List<OrderItem> getOrderItems() {
        return items;
    }

    public static Order create(long userId, List<ProductQuantity> productQuantities, IssuedCoupon coupon) {

        List<OrderItem> orderItems = new ArrayList<>();
        long totalPrice = 0;

        for (ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();

            // 재고 확인
            product.isStockAvailable(quantity);

            long unitPrice = product.getPrice() * quantity;
            totalPrice += unitPrice;
            orderItems.add(new OrderItem(product.getId(), quantity, unitPrice));
        }

        // 쿠폰 적용
        if (coupon.isValid()) {
            totalPrice -= coupon.calculateDiscount(totalPrice);
            totalPrice = Math.max(totalPrice, 0);
        }

        return new Order(userId, orderItems, OrderStatus.CREATED ,totalPrice);
    }

    public record ProductQuantity(Product product, int quantity) {}
}

