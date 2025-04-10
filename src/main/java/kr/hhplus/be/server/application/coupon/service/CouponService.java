package kr.hhplus.be.server.application.coupon.service;


import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final IssuedCouponRepository issuedCouponRepository;
    private final CouponRepository couponRepository;
    // 쿠폰 ID별 Lock 보관소
    private final Map<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public IssuedCoupon getById(long couponId) {
        if (couponId <= 0) {
            return new NoCoupon(); // 기본 객체 반환
        }
        return issuedCouponRepository.findById(couponId);
    }

    public void issueCoupon(long userId, long couponId) {
        // 쿠폰별 락 가져오기
        ReentrantLock lock = lockMap.computeIfAbsent(couponId, id -> new ReentrantLock());
        lock.lock();
        try {
            // 2. 이미 발급받았는지 확인
            if (couponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
            }

            // 3. 발급 수량 초과 확인
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

            int issuedCount = couponRepository.countByCouponId(couponId);

           coupon.isIssueAvailable(issuedCount);

            // 4. 발급
            IssuedCoupon issuedCoupon = new IssuedCoupon(userId, couponId, coupon.getCouponPolicy(),false,false);
            issuedCouponRepository.save(issuedCoupon);
            // 5. 발급 이력 저장
        } finally {
            lock.unlock(); // 💡 꼭 풀어줘야 해!
        }


    }
}
