package kr.hhplus.be.server.interfaces.api.order.dto;


public class OrderItemRequest {
    private long productId;
    private int quantity;

    public OrderItemRequest(long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

}
