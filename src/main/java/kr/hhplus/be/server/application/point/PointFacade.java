package kr.hhplus.be.server.application.point;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.user.repository.UserRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointChargeResult;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;
    private final UserRepository userRepository;



    @Retryable(
            value = CannotAcquireLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public PointChargeResult charge(long userId, long amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        PointHistory resultHistory = pointHistoryService.recordCharge(userId,amount);
        return new PointChargeResult(pointService.charge(userId, amount), resultHistory);
    }

}
