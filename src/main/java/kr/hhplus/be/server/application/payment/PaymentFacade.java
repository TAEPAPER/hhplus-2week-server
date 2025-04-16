package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.service.PaymentService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.point.Point;
import lombok.RequiredArgsConstructor;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PointService pointService;
    private final PointHistoryService pointHistoryService;
    private final ProductService productService;

    public Payment processPayment(long orderId) {
        // 주문 조회 및 검증
        Order order = orderService.getOrderById(orderId);

        //재고 조회
        List<Order.ProductQuantity> productQuantities = productService.getProductsStock(order.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity)));
        //재고 확인
        productService.validateStockAvailability(productQuantities);

        //보유 포인트 조회
        Point point = pointService.getPointByUserId(order.getUser().getId());

        // 결제 처리
        Payment payment = paymentService.processPayment(order, point);

        //포인트 사용 이력 저장
        pointHistoryService.recordUse(order.getUser().getId(), order.getTotalPrice());

        // 재고 차감
        productService.deductStock(productQuantities);


        return payment;
    }

}

