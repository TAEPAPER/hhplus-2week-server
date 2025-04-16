package kr.hhplus.be.server.application.pointHistory.service;

import kr.hhplus.be.server.application.common.ClockHolder;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.application.pointHistory.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final PointRepository pointRepository;

    public PointHistory recordCharge(long userId, long amount) {
        Point point = pointRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다!"));
        PointHistory history = PointHistory.createChargeHistory(point, amount);
        history = pointHistoryRepository.save(history);
        return history;
    }

    public PointHistory recordUse(long userId, long amount) {
        Point point = pointRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다!"));
        PointHistory history = PointHistory.createUseHistory(point, amount);
        history = pointHistoryRepository.save(history);
        return history;
    }
}
