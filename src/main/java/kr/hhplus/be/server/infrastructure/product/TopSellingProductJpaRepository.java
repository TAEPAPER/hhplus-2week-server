package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TopSellingProductJpaRepository extends JpaRepository<TopSellingProduct, Long> {
    Optional<TopSellingProduct> findByProductIdAndAggregatedAt(long productId, LocalDate aggregatedAt);
}
