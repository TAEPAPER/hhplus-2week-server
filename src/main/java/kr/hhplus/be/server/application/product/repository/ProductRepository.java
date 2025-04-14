package kr.hhplus.be.server.application.product.repository;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public interface ProductRepository {

    Product findById(long id);

    List<Product> findAll();

    Product save(Product product);
}
