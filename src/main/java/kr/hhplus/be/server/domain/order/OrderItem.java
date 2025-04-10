package kr.hhplus.be.server.domain.order;

public class OrderItem {
    private final long productId;
    private final int quantity;
    private final long totalPrice;

    public OrderItem(long productId, int quantity, long totalPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}


