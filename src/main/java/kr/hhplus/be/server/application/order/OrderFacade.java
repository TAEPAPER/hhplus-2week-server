package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.domain.order.Order;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;


    public Order order (long userId, Map<Long, Integer> productQuantities, long couponId) {
       //물품별 현재 재고 조회
        List<Order.ProductQuantity> quantities = productService.getProductsStock(productQuantities);

        // 쿠폰 조회
        IssuedCoupon coupon = (couponId > 0) ? couponService.getById(couponId) : new NoCoupon();

        //주문 생성
        Order order = orderService.placeOrder(userId, quantities, coupon);

        return order;
    }


}
