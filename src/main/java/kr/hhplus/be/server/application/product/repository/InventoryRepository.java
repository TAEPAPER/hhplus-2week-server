package kr.hhplus.be.server.application.product.repository;

import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;

import java.util.Optional;

public interface InventoryRepository {
    
    /**
     * ID로 재고 정보를 조회합니다
     * @param productId 상품 ID
     * @return 재고 정보
     */
    Optional<Inventory> findById(long productId);
    
    /**
     * 재고 정보를 저장합니다.
     * @param inventory 저장할 재고 정보
     * @return 저장된 재고 정보
     */
    Inventory save(Inventory inventory);

}