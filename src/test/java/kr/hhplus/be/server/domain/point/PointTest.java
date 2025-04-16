package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void 포인트를_충전하면_포인트가_증가해야_한다(){
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when
        Point charged = point.charge(500L);

        // then
        assertEquals(1L, charged.getUser().getId());
        assertEquals(1500L, charged.getBalance());
    }

    @Test
    void 금액_0원을_충전하면_예외가_발생한다(){
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.charge(0L);
        });

        assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    void 충전_금액이_0보다_작을_경우_예외가_발생한다() {
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.charge(-100L);
        });

        assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    void 포인트를_사용하면_포인트가_감소해야_한다() {
        // given
        User user = User.builder().id(1L).build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when
        Point updatedPoint = point.use(500L);

        // then
        assertEquals(1L, updatedPoint.getUser().getId());
        assertEquals(500L, updatedPoint.getBalance());
    }

    @Test
    void 사용_금액이_0원일_경우_예외가_발생한다() {
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.use(0L);
        });

        assertEquals("사용 금액은 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    void 사용_금액이_0보다_작을_경우_예외가_발생한다() {
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.use(-100L);
        });

        assertEquals("사용 금액은 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    void 사용_금액이_보유_포인트보다_클_경우_예외가_발생한다() {
        // given
        User user = User.builder().id(1L).name("test").build();
        Point point = Point.builder()
                            .userId(1L)
                            .user(user)
                            .balance(1000L)
                            .build();

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            point.use(1500L);
        });

        assertEquals("포인트가 부족합니다.", exception.getMessage());
    }
}
