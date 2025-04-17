package kr.hhplus.be.server.infrastructure.pointHistory;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

}
