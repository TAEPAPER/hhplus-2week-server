package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.payment.repository.PaymentRepository;
import kr.hhplus.be.server.application.payment.service.PaymentService;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.point.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_성공() {
        try (MockedStatic<Payment> mockedPayment = mockStatic(Payment.class)) {
            // Given
            Order order = mock(Order.class);
            Point point = mock(Point.class);

            Payment payment = mock(Payment.class);

            when(order.getTotalPrice()).thenReturn(1000L);
            when(point.use(1000L)).thenReturn(point);
            mockedPayment.when(() -> Payment.createPayment(order)).thenReturn(payment);

            // When
            Payment result = paymentService.processPayment(order, point);

            // Then
            assertNotNull(result);
            verify(point).use(1000L);
            verify(pointRepository).save(point);
            verify(payment).pay();
            verify(paymentRepository).save(payment);
        }
    }
}