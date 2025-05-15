package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product findById(long id) {
        return productJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다!"));
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findByIdWithLock(long id) {
        return Optional.ofNullable(productJpaRepository.findByIdWithLock(id));
    }

    @Override
    public Optional<Product> findByName(String productNm) {
        return productJpaRepository.findByName(productNm);
    }
}
