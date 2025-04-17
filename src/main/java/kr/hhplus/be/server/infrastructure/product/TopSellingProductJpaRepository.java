package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.TopSellingProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopSellingProductJpaRepository extends JpaRepository<TopSellingProduct, Long> {
}
