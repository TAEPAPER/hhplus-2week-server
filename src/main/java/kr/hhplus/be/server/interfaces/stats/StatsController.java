package kr.hhplus.be.server.interfaces.stats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.stats.dto.TopProductResponse;

import java.util.List;

@RestController
@RequestMapping("/stats")
@Tag(name = "통계", description = "인기 상품 통계 API")
public class StatsController {

    @Operation(summary = "인기 상품 Top 5 조회")
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductResponse>> topProducts() {
        return ResponseEntity.ok(List.of(
            new TopProductResponse(1L, "맥북", 200),
            new TopProductResponse(2L, "에어팟", 150),
            new TopProductResponse(3L, "마우스", 120),
            new TopProductResponse(4L, "드론", 110),
            new TopProductResponse(5L, "맥미니", 100)
        ));
    }
}
