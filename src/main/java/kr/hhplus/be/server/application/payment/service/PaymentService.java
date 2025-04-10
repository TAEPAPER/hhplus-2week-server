package kr.hhplus.be.server.application.payment.service;

import kr.hhplus.be.server.application.payment.repository.PaymentRepository;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.point.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;

    public Payment processPayment(Order order,Point point){

        // 포인트 사용 가능 여부 및 차감
        point = point.use(order.getTotalPrice());
        pointRepository.save(point);

        // 결제 처리
        Payment payment = Payment.createPayment(order);
        payment.pay(order);

        // 결제 상태 저장
        paymentRepository.save(payment);

        return payment;
    }
}
