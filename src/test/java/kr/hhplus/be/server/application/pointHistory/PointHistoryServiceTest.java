package kr.hhplus.be.server.application.pointHistory;

import kr.hhplus.be.server.application.common.ClockHolder;
import kr.hhplus.be.server.application.pointHistory.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.domain.TestClockHolder;
import kr.hhplus.be.server.domain.pointHistory.PointHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PointHistoryServiceTest {

    @Mock
    private PointHistoryRepository pointHistoryRepository;


    @Mock
    private ClockHolder clockHolder = new TestClockHolder(1234567890);

    @InjectMocks
    private PointHistoryService pointHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 사용자가_포인트를_충전하면_히스토리가_정상적으로_저장된다() {
        // given
        long userId = 1L;
        long amount = 2000L;
        long currentTimeMillis = 1234567890L;

        when(clockHolder.millis()).thenReturn(currentTimeMillis);

        try (MockedStatic<PointHistory> mockedPointHistory = mockStatic(PointHistory.class)) {
            PointHistory history = mock(PointHistory.class);

            mockedPointHistory.when(() -> PointHistory.createChargeHistory(userId, amount, clockHolder))
                    .thenReturn(history);

            // when
            PointHistory result = pointHistoryService.recordCharge(userId, amount);

            // then

            verify(pointHistoryRepository, times(1)).save(history);
        }
    }

    @Test
    void 사용자가_포인트를_사용하면_히스토리가_정상적으로_저장된다() {
        // given
        long userId = 1L;
        long amount = 1000L;
        long currentTimeMillis = 1234567890L;

        when(clockHolder.millis()).thenReturn(currentTimeMillis);

        try (MockedStatic<PointHistory> mockedPointHistory = mockStatic(PointHistory.class)) {
            PointHistory history = mock(PointHistory.class);

            mockedPointHistory.when(() -> PointHistory.createUseHistory(userId, amount, clockHolder))
                    .thenReturn(history);

            // when
            PointHistory result = pointHistoryService.recordUse(userId, amount);

            // then

            verify(pointHistoryRepository, times(1)).save(history);
        }
    }
}