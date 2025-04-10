package kr.hhplus.be.server.domain.point;

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

    public Point use(long paymentAmount) {
        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }
        long totalAmount = this.amount - paymentAmount;

        if (totalAmount < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        return new Point(this.userId, totalAmount);
    }
}
