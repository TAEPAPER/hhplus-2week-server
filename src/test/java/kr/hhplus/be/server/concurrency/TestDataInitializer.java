package kr.hhplus.be.server.concurrency;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.instancio.Instancio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.instancio.Select.field;

@Service
public class TestDataInitializer {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Coupon initCoupon() {
        Coupon coupon = Instancio.of(Coupon.class)
                .set(field(Coupon::getTotalQuantity), 500)
                .set(field(Coupon::getName), "1000원 할인")
                .set(field(Coupon::getDiscountAmount), 1000)
                .set(field(Coupon::getValidValue), 20)
                .set(field(Coupon::getValidUnit), "days")
                .set(field(Coupon::getType), "FIXED")
                .create();

        em.persist(coupon);
        return coupon;
    }
}
