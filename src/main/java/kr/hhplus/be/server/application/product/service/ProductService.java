package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.product.repository.InventoryRepository;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final TopSellingProductRepository topSellingProductRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String DAILY_POPULAR_KEY = "popular:daily:";

    public Product getById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Order.ProductQuantity> getProductsStock(List<OrderCriteria.OrderProduct> orderProducts) {
        return orderProducts.stream().map(orderProduct -> {
            long productId = orderProduct.getProductId();

            //상품 정보(재고 포함)
            //Product product = productRepository.findById(productId);
            Product product = productRepository.findByIdWithLock(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다!"));
            //주문 수량
            int orderQuantity = orderProduct.getQuantity();

            return new Order.ProductQuantity(product, orderQuantity);
        }).toList();
    }

    // 재고 확인 로직 - 메서드명 변경
    public void validateStockAvailability(List<Order.ProductQuantity> productQuantities) {
        for (Order.ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();
            product.checkStockAvailability(quantity);
        }
    }

    @Transactional
    public void deductStock(List<Order.ProductQuantity> productQuantities) {
        for (Order.ProductQuantity pq : productQuantities) {
            // 상품 정보(재고 포함)
            Product product = pq.product();
            //현재 재고 정보
            Inventory inventory = pq.product().getInventory();

            // 주문 수량
            int quantity = pq.quantity();

            product.deductStock(quantity);
            // 재고 저장
            inventoryRepository.save(product.getInventory());
        }
    }

    @Cacheable(value = "popularProducts", key = "'popular'")
    public List<TopSellingProduct> getPopularProducts() {
        return topSellingProductRepository.findAll();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "popularProducts", key = "'popular'")
    public void refreshPopularProductsCache() {
        // 하루에 한 번 캐시 초기화
        System.out.println("[캐시 초기화] 인기 상품 캐시가 초기화되었습니다.");
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    //redis 스코어 증가
    public void increaseSalesCount(List<OrderItem> items) {
        String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();

        for (OrderItem item : items) {
            long productId = item.getProduct().getId();
            int quantity = item.getQuantity();

            // 랭킹 기준 : 판매량
            // 일일 랭킹
            redisTemplate.opsForZSet().incrementScore(dailyKey, String.valueOf(productId), quantity);

        }
        // TTL 설정 - 일일: 24시간
        redisTemplate.expire(dailyKey, java.time.Duration.ofDays(1));
    }

    // Redis -> DB 영속화 스케줄러
    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void persistDailyPopularProducts() {
        LocalDate targetDate = LocalDate.now().minusDays(1);
        String dailyKey = DAILY_POPULAR_KEY + targetDate;
        Set<String> productIds = redisTemplate.opsForZSet().reverseRange(dailyKey, 0, -1);

        if (productIds != null) {
            for (String productIdStr : productIds) {
                Long productId = Long.parseLong(productIdStr);
                Double salesCount = redisTemplate.opsForZSet().score(dailyKey, productIdStr);

                if (salesCount == null) continue;
                // 기존 데이터 업데이트 또는 새로운 데이터 생성
                TopSellingProduct topSellingProduct = topSellingProductRepository
                        .findByProductIdAndAggregatedAt(productId, targetDate)
                        .map(existingProduct -> existingProduct.increaseTotalSold(salesCount.intValue()))
                        .orElseGet(() -> TopSellingProduct.builder()
                                .product(productRepository.findById(productId))
                                .totalSold(salesCount.intValue())
                                .aggregatedAt(targetDate)
                                .build()
                        );
                // 저장
                topSellingProductRepository.save(topSellingProduct);

            }
        }
        // Redis 키 삭제 (TTL 만료를 기다리지 않고 바로 삭제)
        redisTemplate.delete(dailyKey);
    }

    // Redis에서 Top 10 인기 상품 조회
    public List<TopSellingProduct> getRedisPopularProducts() {
        LocalDate today = LocalDate.now();
        String dailyKey = DAILY_POPULAR_KEY + today;
        Set<String> productIds = redisTemplate.opsForZSet().reverseRange(dailyKey, 0, 9);
        List<TopSellingProduct> popularProducts = new ArrayList<>();

        if (productIds != null) {
            for (String productIdStr : productIds) {
                Long productId = Long.parseLong(productIdStr);
                Double salesCount = redisTemplate.opsForZSet().score(dailyKey, productIdStr);

                if (salesCount != null) {
                    Product product = productRepository.findById(productId);
                    popularProducts.add(
                            TopSellingProduct.builder()
                                    .product(product)
                                    .totalSold(salesCount.intValue())
                                    .aggregatedAt(today)  // LocalDate 사용
                                    .build()
                    );
                }
            }
        }

        return popularProducts;
    }


}
