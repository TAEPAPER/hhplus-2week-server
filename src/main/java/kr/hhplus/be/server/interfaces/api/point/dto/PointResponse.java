package kr.hhplus.be.server.interfaces.api.point.dto;

import java.math.BigDecimal;

// BalanceResponse
public class PointResponse {
    private Long userId;
    private BigDecimal balance;

    public PointResponse(Long userId, BigDecimal balance) {
        this.userId  = userId;
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}