package kr.hhplus.be.server.infrastructure.point;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointJpaRepository  extends JpaRepository<Point, Long> {

    Optional<Point> findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p JOIN FETCH p.user u WHERE p.user.id = :userId") // FOR UPDATE는 제거
    Optional<Point> findByUserIdWithLock(long userId);

}