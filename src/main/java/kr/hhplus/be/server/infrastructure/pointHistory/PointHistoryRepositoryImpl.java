package kr.hhplus.be.server.infrastructure.pointHistory;

import kr.hhplus.be.server.application.pointHistory.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final PointHistoryJpaRepository jpaRepository;

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return jpaRepository.save(pointHistory);
    }


}
