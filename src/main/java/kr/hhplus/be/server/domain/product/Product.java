package kr.hhplus.be.server.domain.product;

public class Product {

    private  long id;
    private  String name;
    private  long price;
    private  Inventory inventory;
    private String description;

    public long getId() { return id; }
    public long getPrice() { return price; }
    public String getName() { return name;}
    public String getDescription() { return description;}

    public void isStockAvailable(int quantity) {
        if (!inventory.hasEnough(quantity)) {
            throw new IllegalStateException("상품 재고 부족");
        }
    }


    public void deduct(int quantity) {
        if (!inventory.hasEnough(quantity)) {
            throw new IllegalStateException("재고 부족으로 차감 불가");
        }
        this.inventory.deduct(quantity);
    }
}
