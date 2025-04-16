package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointChargeResult;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import kr.hhplus.be.server.domain.TestClockHolder;
import kr.hhplus.be.server.domain.pointHistory.TransactionType;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PointFacadeTest {

    @Mock
    private PointService pointService;

    @Mock
    private PointHistoryService pointHistoryService;

    @InjectMocks
    private PointFacade pointFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 사용자가_포인트를_충전하면_Point와_PointHistory가_반환된다() {
        // given
        User user = User.builder()
                        .id(1L)
                        .build();

        long userId = 1L;
        long amount = 1000L;
        TestClockHolder testClockHolder = new TestClockHolder(1234567890);

        Point point = Point.builder()
                .userId(userId)
                .user(user)
                .balance(1000L)
                .build();

        PointHistory pointHistory = new PointHistory(point, amount, TransactionType.CHARGE);

        PointChargeResult expected = new PointChargeResult(point, pointHistory);

        when(pointService.charge(userId, amount)).thenReturn(point);
        when(pointHistoryService.recordCharge(userId, amount)).thenReturn(pointHistory);

        // when
        PointChargeResult result = pointFacade.charge(userId, amount);

        // then
        assertThat(result.getUpdatedPoint()).isEqualTo(point);
        assertThat(result.getPointHistory()).isEqualTo(pointHistory);
        verify(pointService, times(1)).charge(userId, amount);
        verify(pointHistoryService, times(1)).recordCharge(userId, amount);
    }
}