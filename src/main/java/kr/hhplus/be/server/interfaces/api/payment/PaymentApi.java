package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentRequest;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제", description = "결제")
public interface PaymentApi {

    @Operation(summary = "결제 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "성공 예시", value = """
                    {
                        "paymentId": 123,
                        "status": "SUCCESS",
                        "amount": 50000
                    }
                    """)
            })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 요청", value = """
                    {
                        "code": "4001",
                        "message": "결제 정보가 유효하지 않습니다."
                    }
                    """)
            })),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "서버 오류", value = """
                    {
                        "code": "5001",
                        "message": "결제 처리 중 오류가 발생했습니다."
                    }
                    """)
            }))
    })
    ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request);
}