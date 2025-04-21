package kr.hhplus.be.server.interfaces.api.point.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointChargeRequest {

    private Long userId;
    private Long amount;

    @JsonCreator
    public PointChargeRequest(@JsonProperty("userId") Long userId, @JsonProperty("amount") Long amount) {
        this.userId = userId;
        this.amount = amount;
    }
}