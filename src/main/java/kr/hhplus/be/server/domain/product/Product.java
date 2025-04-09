package kr.hhplus.be.server.domain.product;

import lombok.Getter;

@Getter
public class Product {

    private final long id;
    private final String name;
    private final long price;
    private final String description;


    public Product(long id, String name, long price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }




}
