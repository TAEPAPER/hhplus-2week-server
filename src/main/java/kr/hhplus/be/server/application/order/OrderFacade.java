package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.application.user.service.UserService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final CouponService couponService;
    private final UserService UserService;;


    public Order order (long userId, Map<Long, Integer> productQuantities, long couponId) {
        //사용자 정보 조회
        User user = UserService.getUserById(userId);
        //물품별 현재 재고 조회
        List<Order.ProductQuantity> quantities = productService.getProductsStock(productQuantities);

        // 쿠폰 조회
        IssuedCoupon coupon = (couponId > 0) ? couponService.getById(couponId) : new NoCoupon();

        //주문 생성
        Order order = orderService.placeOrder(user, quantities, coupon);

        return order;
    }


}
