package kr.hhplus.be.server.application.point.service;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point charge(long userId, long amount) {
        Point point = pointRepository.findById(userId).orElseThrow();
        pointRepository.save(point.charge(amount));
        return point;
    }




}
