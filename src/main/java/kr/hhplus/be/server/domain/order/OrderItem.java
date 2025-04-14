package kr.hhplus.be.server.domain.order;

public class OrderItem {
    private long productId;
    private int quantity;
    private long unitPrice;

    public OrderItem(long productId, int quantity, long unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}


