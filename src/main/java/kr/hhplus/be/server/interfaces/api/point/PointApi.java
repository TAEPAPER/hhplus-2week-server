package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.point.dto.PointChargeRequest;
import kr.hhplus.be.server.interfaces.api.point.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포인트", description = "포인트 충전")
public interface PointApi {
    @Operation(summary = "포인트를 충전합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "충전 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "성공 예시", value = """
                            {
                                "userId": 1,
                                "balance": 15000
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "0원 또는 음수 충전", value = """
                            {
                                "code": "4001",
                                "message": "충전 금액은 0보다 커야 합니다."
                            }
                            """)
            }))
    })

    ResponseEntity<PointResponse> charge(@RequestBody @Validated PointChargeRequest request);

}
