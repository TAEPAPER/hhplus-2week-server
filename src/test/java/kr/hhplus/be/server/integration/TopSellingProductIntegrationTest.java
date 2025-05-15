package kr.hhplus.be.server.integration;
import jakarta.annotation.Nullable;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.domain.user.User;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.GenericContainer;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TopSellingProductIntegrationTest {

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.0.12")
            .withExposedPorts(6379);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TopSellingProductRepository topSellingProductRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String DAILY_POPULAR_KEY = "popular:daily:";

    @BeforeEach
    void setUp() {
        int redisPort = redisContainer.getMappedPort(6379);
        System.setProperty("spring.redis.host", "localhost");
        System.setProperty("spring.redis.port", String.valueOf(redisPort));

        // 테스트 데이터 초기화
        productRepository.save(new Product("상품A", 10000, "설명A"));
        productRepository.save(new Product("상품B", 20000, "설명B"));
        productRepository.save(new Product("상품C", 30000, "설명C"));
    }

    @Test
    @Transactional
    void testIncreaseSalesCount() {

        User user = new User(1L, "testUser");
        Order order = new Order(user, List.of(), OrderStatus.CREATED, 10000, null);

        // 상품 조회
        Product productA = productRepository.findByName("상품A").orElseThrow();
        Product productB = productRepository.findByName("상품B").orElseThrow();

        // 판매량 증가
        productService.increaseSalesCount(List.of(
                new OrderItem(order, productA, 10, 2000)
        ));

        // Redis 데이터 검증
        String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();
        Double productAScore = redisTemplate.opsForZSet().score(dailyKey, String.valueOf(productA.getId()));
        Double productBScore = redisTemplate.opsForZSet().score(dailyKey, String.valueOf(productB.getId()));

        assertThat(productAScore).isEqualTo(10.0);
        assertThat(productBScore).isEqualTo(5.0);
    }

    @Test
    @Transactional
    void testPersistDailyPopularProducts() {
        User user = new User(1L, "testUser");
        Order order = new Order(user, List.of(), OrderStatus.CREATED, 10000, null);

        // 상품 조회
        Product productA = productRepository.findByName("상품A").orElseThrow();

        // 판매량 증가
        productService.increaseSalesCount(List.of(
                new OrderItem(order, productA, 10, 2000)
        ));

        // 인기 상품 저장
        productService.persistDailyPopularProducts();

        // Redis 데이터 검증
        String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();
        List<TopSellingProduct> popularProducts = topSellingProductRepository.findAll();

        assertThat(popularProducts).hasSize(1);
        assertThat(popularProducts.get(0).getProduct().getId()).isEqualTo(productA.getId());
    }

    @Test
    void testTTLSetting() {
        // 상품 조회
        Product productA = productRepository.findByName("상품A").orElseThrow();

        // 판매량 증가
        productService.increaseSalesCount(List.of(
                new OrderItem(null, productA, 10, 2000)
        ));

        // TTL 검증
        String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();
        Long ttl = redisTemplate.getExpire(dailyKey, TimeUnit.SECONDS);
        assertThat(ttl).isGreaterThan(0);
    }

    @Test
    @Transactional
    void testTop10PopularProducts() {
        // 여러 상품 생성
        Product productA = productRepository.findByName("상품A").orElseThrow();
        Product productB = productRepository.findByName("상품B").orElseThrow();
        Product productC = productRepository.findByName("상품C").orElseThrow();

        // 판매
    }
}