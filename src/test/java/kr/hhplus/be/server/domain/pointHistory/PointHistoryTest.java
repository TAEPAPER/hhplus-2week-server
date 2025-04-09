package kr.hhplus.be.server.domain.pointHistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    void 포인트_충전_이력을_생성한다() {
        //given
        TestClockHolder testClockHolder = new TestClockHolder(1234567890);
        PointHistory expected = new PointHistory(1L, 1000L, TransactionType.CHARGE, testClockHolder.millis());

        //when
        PointHistory actual = PointHistory.createChargeHistory(1L, 1000L, testClockHolder);

        //then
        assertEquals(expected, actual);

    }

}
