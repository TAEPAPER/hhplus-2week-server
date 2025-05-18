package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CouponServiceIntegrationTest {

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.12")
            .withExposedPorts(6379);

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String COUPON_STOCK_KEY = "coupon:%d:stock";
    private static final String COUPON_ISSUED_KEY = "coupon:%d:issued";

    private Coupon testCoupon;

    @BeforeEach
    void setUp() {
        int redisPort = redisContainer.getMappedPort(6379);
        System.setProperty("spring.redis.host", "localhost");
        System.setProperty("spring.redis.port", String.valueOf(redisPort));

        // 쿠폰 초기화
        testCoupon = Coupon.builder()
                .name("할인쿠폰")
                .discountAmount(5000)
                .totalQuantity(10)
                .validValue(30)
                .validUnit("days")
                .type("FIXED")
                .build();
        couponRepository.save(testCoupon);

        // Redis 초기화
        String stockKey = String.format(COUPON_STOCK_KEY, testCoupon.getId());
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForList().leftPush(stockKey, String.valueOf(i + 1));
        }
    }

    @Test
    @Transactional
    void testIssueCouponRedis_Success() {
        // 쿠폰 발급
        couponService.issueCouponRedis(101L, testCoupon.getId());
        couponService.issueCouponRedis(102L, testCoupon.getId());

        // Redis 검증
        String issuedKey = String.format(COUPON_ISSUED_KEY, testCoupon.getId());
        Set<Object> issuedUsers = redisTemplate.opsForSet().members(issuedKey);
        assertThat(issuedUsers).contains("101", "102");

        // 재고 검증
        String stockKey = String.format(COUPON_STOCK_KEY, testCoupon.getId());
        long remainingStock = redisTemplate.opsForList().size(stockKey);
        assertThat(remainingStock).isEqualTo(8);
    }

    @Test
    @Transactional
    void testIssueCouponRedis_Duplicate() {
        // 첫 발급 성공
        couponService.issueCouponRedis(201L, testCoupon.getId());

        // 중복 발급 시도
        assertThatThrownBy(() -> couponService.issueCouponRedis(201L, testCoupon.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 발급받은 쿠폰입니다.");
    }

    @Test
    @Transactional
    void testIssueCouponRedis_StockDepleted() {
        // 10개 모두 발급
        for (int i = 1; i <= 10; i++) {
            couponService.issueCouponRedis(300L + i, testCoupon.getId());
        }

        // 재고 부족 시도
        assertThatThrownBy(() -> couponService.issueCouponRedis(400L, testCoupon.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("쿠폰이 소진되었습니다.");
    }

    @Test
    @Transactional
    void testPersistIssuedUserInfo() {
        // 쿠폰 발급
        couponService.issueCouponRedis(501L, testCoupon.getId());
        couponService.issueCouponRedis(502L, testCoupon.getId());

        // Redis -> DB 영속화
        couponService.persistIssuedUserInfo();

        // DB 검증
        List<IssuedCoupon> issuedCoupons = issuedCouponRepository.findAll();
        assertThat(issuedCoupons).hasSize(2);

        List<Long> userIds = issuedCoupons.stream().map(IssuedCoupon::getUserId).collect(Collectors.toList());
        assertThat(userIds).containsExactlyInAnyOrder(501L, 502L);

        // 만료일 검증
        for (IssuedCoupon issuedCoupon : issuedCoupons) {
            assertThat(issuedCoupon.getExpiredAt()).isEqualTo(issuedCoupon.getIssuedAt().plusDays(30));
        }

        // Redis 데이터 삭제 검증
        String issuedKey = String.format(COUPON_ISSUED_KEY, testCoupon.getId());
        Set<Object> issuedUsers = redisTemplate.opsForSet().members(issuedKey);
        assertThat(issuedUsers).isEmpty();
    }

    @Test
    @Transactional
    void testCouponExpiryValidation() {
        // 쿠폰 발급
        couponService.issueCouponRedis(601L, testCoupon.getId());

        // 영속화
        couponService.persistIssuedUserInfo();

        // 쿠폰 만료 확인
        IssuedCoupon issuedCoupon = issuedCouponRepository.findAll().get(0);
        assertThat(issuedCoupon.isValid()).isTrue();

        // 만료일 지나기 (임의로 만료일 수정)
        issuedCoupon.getExpiredAt().minusDays(31);
        assertThat(issuedCoupon.isValid()).isFalse();
    }
}
