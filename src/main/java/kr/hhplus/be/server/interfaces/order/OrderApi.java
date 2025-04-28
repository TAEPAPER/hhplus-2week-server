package kr.hhplus.be.server.interfaces.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "주문", description = "주문")
public interface OrderApi {

    @Operation(summary = "주문 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "성공 예시", value = """
                    {
                        "orderId": 123,
                        "userId": 1,
                        "totalPrice": 50000,
                        "items": [
                            {
                                "productId": 101,
                                "quantity": 2,
                                "price": 25000
                            }
                        ]
                    }
                    """)
            })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 요청", value = """
                    {
                        "code": "4001",
                        "message": "주문 항목이 비어 있습니다."
                    }
                    """)
            })),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "상품 없음", value = """
                    {
                        "code": "4041",
                        "message": "요청한 상품을 찾을 수 없습니다."
                    }
                    """)
            }))
    })
    ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request);
}