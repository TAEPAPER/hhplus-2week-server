package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentRequest;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.hhplus.be.server.application.payment.PaymentFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController implements PaymentApi{

    private final PaymentFacade paymentFacade;

    @Override
    @RequestMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(PaymentRequest request) {

       Payment payment = paymentFacade.processPayment(request.getOrderId());

        return ResponseEntity.ok(new PaymentResponse(payment));
    }
}
