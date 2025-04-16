package kr.hhplus.be.server.application.point;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointChargeResult;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public PointChargeResult charge(long userId, long amount) {
        Point pointResult = pointService.charge(userId, amount);
        PointHistory resultHistory = pointHistoryService.recordCharge(userId,amount);
        return new PointChargeResult(pointResult, resultHistory);
    }

}
