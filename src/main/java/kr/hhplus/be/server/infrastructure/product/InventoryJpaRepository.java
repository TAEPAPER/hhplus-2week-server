package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryJpaRepository extends JpaRepository<Inventory, Long> {

}