package kr.hhplus.be.server.application.coupon.service;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceTest {

    @Mock
    private IssuedCouponRepository issuedCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void issueCoupon_Success() {
        // Given
        long userId = 1L;
        long couponId = 1L;
        Coupon coupon = mock(Coupon.class);

        when(couponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(false);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(couponRepository.countByCouponId(couponId)).thenReturn(3);
        doNothing().when(coupon).isIssueAvailable(3);

        // When
        assertDoesNotThrow(() -> couponService.issueCoupon(userId, couponId));

        // Then
        verify(issuedCouponRepository, times(1)).save(any(IssuedCoupon.class));
    }

    @Test
    void issueCoupon_AlreadyIssued() {
        // Given
        long userId = 1L;
        long couponId = 1L;

        when(couponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(true);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> couponService.issueCoupon(userId, couponId));
        assertEquals("이미 발급받은 쿠폰입니다.", exception.getMessage());
    }

    @Test
    void issueCoupon_CouponNotFound() {
        // Given
        long userId = 1L;
        long couponId = 1L;

        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> couponService.issueCoupon(userId, couponId));
        assertEquals("쿠폰이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void issueCoupon_ExceedsQuantity() {
        // Given
        long userId = 1L;
        long couponId = 1L;
        Coupon coupon = mock(Coupon.class);

        when(couponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(false);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(couponRepository.countByCouponId(couponId)).thenReturn(5);
        doThrow(new IllegalArgumentException("쿠폰 발급 수량이 초과되었습니다.")).when(coupon).isIssueAvailable(5);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> couponService.issueCoupon(userId, couponId));
        assertEquals("쿠폰 발급 수량이 초과되었습니다.", exception.getMessage());
    }
}