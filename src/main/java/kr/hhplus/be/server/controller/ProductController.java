package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "상품", description = "상품 조회 API")
public class ProductController {

    private final List<ProductResponse> products = List.of(
        new ProductResponse(1L, "맥북", new BigDecimal("2000000"), 10, "애플 노트북"),
        new ProductResponse(2L, "에어팟", new BigDecimal("200000"), 30, "애플 이어폰")
    );

    @Operation(summary = "전체 상품 조회")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(products);
    }
}
