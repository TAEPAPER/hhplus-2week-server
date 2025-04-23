package kr.hhplus.be.server.interfaces.point.dto;

import kr.hhplus.be.server.domain.point.Point;

import java.math.BigDecimal;

// BalanceResponse
public class PointResponse {

    private long userId;
    private long balance;

    public PointResponse(long userId, long amount) {
        this.userId = userId;
        this.balance = amount;
    }

    public long getUserId() {
        return userId;
    }

    public long getBalance() {
        return balance;
    }
}