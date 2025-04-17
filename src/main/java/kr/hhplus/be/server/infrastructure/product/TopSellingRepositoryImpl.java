package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TopSellingRepositoryImpl implements TopSellingProductRepository {

    private final TopSellingProductJpaRepository topSellingProductJpaRepository;

    @Override
    public List<TopSellingProduct> findAll() {
        return topSellingProductJpaRepository.findAll();
    }
}
