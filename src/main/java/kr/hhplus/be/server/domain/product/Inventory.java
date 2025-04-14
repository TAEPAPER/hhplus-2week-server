package kr.hhplus.be.server.domain.product;

public class Inventory {
    private int stock;

    public Inventory(int stock) {
        this.stock = stock;
    }

    public boolean hasEnough(int quantity) {
        return stock >= quantity;
    }

    public void deduct(int quantity) {
        if (!hasEnough(quantity)) {
            throw new IllegalStateException("재고 부족으로 차감 불가");
        }
        this.stock -= quantity;
    }
}
