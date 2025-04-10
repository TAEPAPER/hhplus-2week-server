package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.service.PaymentService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;


    public void order (long userId, Map<Long, Integer> productQuantities, long couponId) {
       //물품별 현재 재고 조회
        List<Order.ProductQuantity> quantities = productService.getProductsStock(productQuantities);

        //쿠폰 조회
        Coupon coupon = couponService.getById(couponId);

        //주문 생성
        Order order = orderService.placeOrder(userId, quantities, coupon);
    }


}
