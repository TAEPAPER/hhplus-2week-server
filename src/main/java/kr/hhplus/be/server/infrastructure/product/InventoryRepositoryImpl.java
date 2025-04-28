package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.application.product.repository.InventoryRepository;
import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    public Optional<Inventory> findById(long id) {
        return inventoryJpaRepository.findById(id);
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryJpaRepository.save(inventory);
    }

}