package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import kr.hhplus.be.server.interfaces.api.point.dto.PointResponse;
import lombok.Getter;

@Getter
public class PointChargeResult {

    private Point updatedPoint;
    private PointHistory pointHistory;

    public PointChargeResult(Point updatedPoint, PointHistory pointHistory) {
        this.updatedPoint = updatedPoint;
        this.pointHistory = pointHistory;
    }

    public PointResponse toDto() {
        return new PointResponse(
                updatedPoint.getUser().getId(),
                updatedPoint.getBalance()
        );
    }
}
