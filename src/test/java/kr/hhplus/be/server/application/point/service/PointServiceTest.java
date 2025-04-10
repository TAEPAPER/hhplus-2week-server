package kr.hhplus.be.server.application.point.service;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 사용자가_포인트를_충전하면_포인트가_증가해야_한다() {
        // given
        long userId = 1L;
        long initialAmount = 1000L;
        long chargeAmount = 2000L;

        Point existingPoint = new Point(userId, initialAmount);
        Point updatedPoint = new Point(userId, initialAmount + chargeAmount);

        when(pointRepository.findById(userId)).thenReturn(Optional.of(existingPoint));
        when(pointRepository.save(any(Point.class))).thenReturn(updatedPoint);

        // when
        Point result = pointService.charge(userId, chargeAmount);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getAmount()).isEqualTo(initialAmount + chargeAmount);

        verify(pointRepository, times(1)).findById(userId);
        verify(pointRepository, times(1)).save(any(Point.class));
    }

    @Test
    void 존재하지_않는_사용자의_포인트를_충전하려고_하면_예외가_발생해야_한다() {
        // given
        long userId = 1L;
        long chargeAmount = 2000L;

        when(pointRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pointService.charge(userId, chargeAmount);
        });

        assertThat(exception.getMessage()).isEqualTo("포인트 정보가 없습니다!");

        verify(pointRepository, times(1)).findById(userId);
        verify(pointRepository, never()).save(any(Point.class));
    }

    @Test
    void 사용자의_포인트를_정상적으로_조회한다() {
        // given
        long userId = 1L;
        Point point = new Point(userId, 1000L);

        when(pointRepository.findById(userId)).thenReturn(Optional.of(point));

        // when
        Point result = pointService.getPointByUserId(userId);

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getAmount()).isEqualTo(1000L);

        verify(pointRepository, times(1)).findById(userId);
    }


}