package kr.hhplus.be.server.dto.response;

import java.math.BigDecimal;

// ProductResponse
public class ProductResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private int stock;
    private String description;

    public ProductResponse(long productId, String name, BigDecimal price, int stock, String description) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }
}