package kr.hhplus.be.server.application.product.repository;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product findById(long id);

    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findByIdWithLock(long id);
}
