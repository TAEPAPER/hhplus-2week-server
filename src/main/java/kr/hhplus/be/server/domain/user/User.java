package kr.hhplus.be.server.domain.user;


import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class User {

    protected User() {}  // JPA용

    public User(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    private String name;

    @Version
    private int version; // 버전 필드 추가

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Point point;

    @Builder
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }


}
