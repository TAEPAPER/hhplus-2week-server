package kr.hhplus.be.server.application.pointHistory.repository;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;


public interface PointHistoryRepository {
    //포인트 이력 저장
    PointHistory save(PointHistory pointHistory);

}
