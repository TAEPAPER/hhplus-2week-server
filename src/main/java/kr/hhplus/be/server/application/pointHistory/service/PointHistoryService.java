package kr.hhplus.be.server.application.pointHistory.service;

import kr.hhplus.be.server.application.common.ClockHolder;
import kr.hhplus.be.server.application.pointHistory.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final ClockHolder clockHolder;

    public PointHistory recordCharge(long userId, long amount) {
        PointHistory history = PointHistory.createChargeHistory(userId, amount, clockHolder);
        pointHistoryRepository.save(history);
        return history;
    }

    public PointHistory recordUse(long userId, long amount) {
        PointHistory history = PointHistory.createUseHistory(userId, amount, clockHolder);
        pointHistoryRepository.save(history);
        return history;
    }
}
