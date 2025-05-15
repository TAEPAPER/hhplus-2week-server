package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TopSellingRepositoryImpl implements TopSellingProductRepository {

    private final TopSellingProductJpaRepository topSellingProductJpaRepository;

    @Override
    public List<TopSellingProduct> findAll() {
        return topSellingProductJpaRepository.findAll();
    }

    @Override
    public Optional<TopSellingProduct> findByProductIdAndAggregatedAt(Long productId, LocalDate localDate) {
        return topSellingProductJpaRepository.findByProductIdAndAggregatedAt(productId, localDate);
    }

    @Override
    public void save(TopSellingProduct topSellingProduct) {
        topSellingProductJpaRepository.save(topSellingProduct);
    }


}
