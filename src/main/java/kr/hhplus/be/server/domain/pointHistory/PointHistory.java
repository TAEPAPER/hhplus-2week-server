package kr.hhplus.be.server.domain.pointHistory;

import kr.hhplus.be.server.application.common.ClockHolder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class PointHistory {

    private long userId;
    private long amount;
    private TransactionType type;
    private long updateMillis;

    public PointHistory(long userId, long amount, TransactionType type, long updateMillis) {
        this.userId = userId;
        this. amount = amount;
        this.type = type;
        this.updateMillis = updateMillis;
    }

    //충전 이력 생성
    public static PointHistory createChargeHistory(long userId, long amount, ClockHolder clockHolder) {
        return new PointHistory(userId, amount, TransactionType.CHARGE, clockHolder.millis());
    }

    //사용 이력 생성
    public static PointHistory createUseHistory(long userId, long amount,ClockHolder clockHolder ) {
        return new PointHistory(userId, amount, TransactionType.USE, clockHolder.millis());
    }

}
