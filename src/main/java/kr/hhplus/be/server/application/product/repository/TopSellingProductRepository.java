package kr.hhplus.be.server.application.product.repository;

import kr.hhplus.be.server.domain.product.TopSellingProduct;

import java.util.List;

public interface TopSellingProductRepository {
    List<TopSellingProduct> findAll();
}
