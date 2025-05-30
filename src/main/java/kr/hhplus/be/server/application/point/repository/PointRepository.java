package kr.hhplus.be.server.application.point.repository;

import kr.hhplus.be.server.domain.point.Point;

import java.util.Optional;

public interface PointRepository {

    Optional<Point> findById(long id);

    Point save(Point point);

    void deleteAll();

    Optional<Point> findByUserId(long userId);

    Optional<Point> findByUserIdWithLock(long id);
}
