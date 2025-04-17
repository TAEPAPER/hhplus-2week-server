package kr.hhplus.be.server.domain.coupon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CouponPolicyResolver implements ApplicationContextAware {

    private static Map<String, CouponPolicy> policyMap;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        policyMap = context.getBeansOfType(CouponPolicy.class)
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toUpperCase(),
                        Map.Entry::getValue
                ));
    }

    public static CouponPolicy resolve(String type) {
        CouponPolicy policy = policyMap.get(type.toUpperCase());
        if (policy == null) {
            throw new IllegalArgumentException("지원하지 않는 쿠폰 타입: " + type);
        }
        return policy;
    }
}
