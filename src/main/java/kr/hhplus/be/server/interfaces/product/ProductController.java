package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import kr.hhplus.be.server.interfaces.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(Long id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(new ProductResponse(product));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(
                products.stream().map(ProductResponse::new).toList()
        );
    }

    //인기상품 조회
    @Override
    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>> getPopular() {
        List<TopSellingProduct> topSellingProducts = productService.getPopularProducts();
        return ResponseEntity.ok(
                topSellingProducts.stream().filter(top -> top.getProduct() != null)
                        .map(product -> new ProductResponse(product.getProduct()))
                        .toList()

        );

    }
}
