package kr.hhplus.be.server.interfaces.api.point.dto;

import java.math.BigDecimal;

public class PointChargeRequest {

    private Long userId;
    private BigDecimal amount;

    // 👇 필수: 기본 생성자 (스프링이 역직렬화할 때 필요)
    public PointChargeRequest() {}

    public PointChargeRequest(Long userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    // (선택) setter 도 추가 가능
}
