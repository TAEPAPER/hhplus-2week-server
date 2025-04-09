package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.pointHistory.PointHistory;

public class Point {

    private long userId;
    private long amount;

    public Point(long userId, long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public long getUserId(){
        return this.userId;
    }

    public long getAmount(){
        return this.amount;
    }

    //포인트를 충전한다
    public Point charge(long amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        long totalAmount = this.amount + amount;

        return new Point(this.userId, totalAmount);
    }

}
