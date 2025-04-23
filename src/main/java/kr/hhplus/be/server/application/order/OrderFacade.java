package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.application.user.service.UserService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;
    private final UserService UserService;;

    @Transactional
    public Order order (OrderCriteria.OrderPayment orderPayment) {
        //사용자 정보 조회
        User user = UserService.getUserById(orderPayment.getUserId());

        //물품별 현재 재고 조회
        List<Order.ProductQuantity> quantities = productService.getProductsStock(orderPayment.getProducts());

        // 쿠폰 조회
        IssuedCoupon coupon = (orderPayment.getCouponId() > 0) ? couponService.getById(orderPayment.getCouponId()) : new NoCoupon();

        //주문 생성
        Order order = orderService.placeOrder(user, quantities, coupon);

        return order;
    }


}
