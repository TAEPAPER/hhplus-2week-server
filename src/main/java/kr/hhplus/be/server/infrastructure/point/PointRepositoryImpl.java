package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.application.point.repository.PointRepository;

import kr.hhplus.be.server.domain.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository jpaRepository;

    @Override
    public Optional<Point> findById(long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Point save(Point point) {
        return jpaRepository.save(point);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<Point> findByUserId(long userId) {
        return jpaRepository.findByUserId(userId);
    }


}
