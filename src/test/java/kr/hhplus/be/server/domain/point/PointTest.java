package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void 포인트를_충전하면_포인트가_증가해야_한다(){
        // given
        Point point = new Point(1L, 1000L);

        // when
        Point charged = point.charge(500L);

        // then
        assertEquals(1L, charged.getUserId());
        assertEquals(1500L, charged.getAmount());
    }

    @Test
    void 금액_0원을_충전하면_예외가_발생한다(){
        // given
        Point point = new Point(1L, 1000L);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.charge(0L);
        });

        assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    void 충전_금액이_0보다_작을_경우_예외가_발생한다() {
        // given
        Point point = new Point(1L, 1000L);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.charge(-100L);
        });

        assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
    }
}
