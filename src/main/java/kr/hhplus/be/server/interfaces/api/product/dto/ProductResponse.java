package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.product.Product;

public class ProductResponse {

    private final long id;
    private final String name;
    private final long price;
    private final String description;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
    }

}
