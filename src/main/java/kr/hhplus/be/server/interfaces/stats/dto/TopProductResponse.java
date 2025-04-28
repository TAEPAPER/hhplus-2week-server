package kr.hhplus.be.server.interfaces.stats.dto;

// TopProductResponse
public class TopProductResponse {
    private Long productId;
    private String productName;
    private int totalSold;

    public TopProductResponse(long productId, String productName, int totalSold) {
        this.productId = productId;
        this.productName = productName;
        this.totalSold = totalSold;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotalSold() {
        return totalSold;
    }

}