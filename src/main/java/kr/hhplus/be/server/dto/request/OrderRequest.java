package kr.hhplus.be.server.dto.request;


import java.util.List;

public class OrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    // inner class
    public static class OrderItemRequest {
        private Long productId;
        private Long productOptionId;
        private int quantity;

        public long getQuantity() {
            return quantity;
        }
    }
}