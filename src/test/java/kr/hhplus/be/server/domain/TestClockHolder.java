package kr.hhplus.be.server.domain;

import kr.hhplus.be.server.application.common.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long millis;

    @Override
    public long millis() {
        return millis;
    }
}
