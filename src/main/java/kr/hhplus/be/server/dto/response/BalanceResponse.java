package kr.hhplus.be.server.dto.response;

import java.math.BigDecimal;

// BalanceResponse
public class BalanceResponse {
    private Long userId;
    private BigDecimal balance;

    public BalanceResponse(Long userId, BigDecimal balance) {
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