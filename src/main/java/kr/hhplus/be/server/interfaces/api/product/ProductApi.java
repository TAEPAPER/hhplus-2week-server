package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품", description = "상품 조회 API")
@RequestMapping("/products")
public interface ProductApi {

    @Operation(summary = "상품 단건 조회")
    ResponseEntity<ProductResponse> get(@PathVariable("id") Long id);

    @Operation(summary = "전체 상품 조회")
    ResponseEntity<List<ProductResponse>> getAll();

    @Operation(summary = "인기 상품 조회")
    ResponseEntity<List<ProductResponse>> getPopular();
}
