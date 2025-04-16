package kr.hhplus.be.server.domain.pointHistory;

import kr.hhplus.be.server.domain.TestClockHolder;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    void 포인트_사용_이력을_생성한다() {
        //given
        User user = User.builder()
                        .id(1L)
                        .name("test")
                        .build();
        Point point = Point.builder()
                        .userId(1L)
                        .user(user)
                        .balance(1000L)
                        .build();

        PointHistory expected = new PointHistory(point, 500L, TransactionType.USE);

        //when
        PointHistory actual = PointHistory.createUseHistory(point, 500L);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void 포인트_충전_이력을_생성한다() {
        //given
        User user = User.builder()
                .id(1L)
                .name("test")
                .build();
        Point point = Point.builder()
                .userId(1L)
                .user(user)
                .balance(1000L)
                .build();

        PointHistory expected = new PointHistory(point, 500L, TransactionType.CHARGE);

        //when
        PointHistory actual = PointHistory.createChargeHistory(point, 500L);

        //then
        assertEquals(expected, actual);

    }

}
