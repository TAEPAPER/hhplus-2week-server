package kr.hhplus.be.server.application.point.service;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.Point;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    private PointService pointService;

    /*@Test
    void 사용자가_포인트를_충전하고_이력에_삽입한다(){

        //given
        long userId = 1L;
        long amount = 1000L;
        long charge = 2000L;

        Point point = Point.builder()
                           .userId(userId)
                           .amount(amount)
                           .build();
        Point expected =  Point.builder()
                         .userId(userId)
                         .amount(amount + charge)
                         .build();

        when(pointRepository.findById(1L)).thenReturn(
            Optional.ofNullable(point)
        );

        when(pointRepository.save(expected)).thenReturn(expected);


        //when
        Point totalPoint = pointService.charge(1L, 2000L);

        //then

        assertThat(expected.getUserId()).isEqualTo(userId);
        assertThat(expected.getAmount()).isEqualTo(amount + charge);
    }*/


}
