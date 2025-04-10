package kr.hhplus.be.server.interfaces.api.point;


import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.interfaces.api.point.dto.PointChargeRequest;
import kr.hhplus.be.server.interfaces.api.point.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController implements PointApi {

    private final PointFacade pointFacade;

    @Override
    @PostMapping("/charge")
    public ResponseEntity<PointResponse> charge(PointChargeRequest request) {
        return ResponseEntity.ok(
                pointFacade.charge(request.getUserId(), request.getAmount()).toDto()
        );
    }

}
