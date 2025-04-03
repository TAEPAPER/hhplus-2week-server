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


}