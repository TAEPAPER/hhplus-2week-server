package kr.hhplus.be.server.application.point.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.*;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Point charge(long userId, long amount) {
        Point point = pointRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다!"));
        return point.charge(amount);
    }

    public Point getPointByUserId(long userId) {
        return pointRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("포인트 정보가 없습니다!"));
    }


}
