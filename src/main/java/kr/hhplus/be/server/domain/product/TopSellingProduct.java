package kr.hhplus.be.server.domain.product;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class TopSellingProduct implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name="total_sold", nullable = false)
    private int totalSold;

    @CreatedDate
    @CreationTimestamp
    private LocalDateTime aggregatedAt;


    public Product getProduct() {
        return product;
    }
}
