package kr.hhplus.be.server.domain.product;

import lombok.Getter;

public class Product {

    private  long id;
    private  String name;
    private  long price;
    private  Inventory inventory;


    public void isStockAvailable(int quantity) {
        if (!inventory.hasEnough(quantity)) {
            throw new IllegalStateException("상품 재고 부족");
        }
    }

    public long getId() { return id; }
    public long getPrice() { return price; }
}
