package kr.hhplus.be.server.interfaces.api.point.dto;

import lombok.Builder;

import java.math.BigDecimal;

public class PointChargeRequest {

    private long userId;
    private long amount;

    @Builder
    public PointChargeRequest(Long userId, long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public long getAmount() {
        return amount;
    }

}
