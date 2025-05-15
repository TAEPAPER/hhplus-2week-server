package kr.hhplus.be.server.application.product.repository;

import kr.hhplus.be.server.domain.product.TopSellingProduct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TopSellingProductRepository {
    List<TopSellingProduct> findAll();
    Optional<TopSellingProduct> findByProductIdAndAggregatedAt(Long productId, LocalDate localDate);
    void save(TopSellingProduct topSellingProduct);
}
