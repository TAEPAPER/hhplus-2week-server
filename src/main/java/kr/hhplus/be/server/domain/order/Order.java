package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {

    private final long userId;
    private final OrderStatus orderStatus;
    private final List<OrderItem> items;

    public Order( long userId, List<OrderItem> items, OrderStatus orderStatus) {
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.items = items;
    }

    public static Order create(long userId, List<ProductQuantity> productQuantities) {

        List<OrderItem> orderItems = new ArrayList<>();

        for (ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();

            // 재고 확인
            product.isStockAvailable(quantity);

            long totalPrice = product.getPrice() * quantity;
            orderItems.add(new OrderItem(product.getId(), quantity, totalPrice));
        }

        return new Order(userId, orderItems, OrderStatus.CREATED);
    }
    public record ProductQuantity(Product product, int quantity) {}
}

