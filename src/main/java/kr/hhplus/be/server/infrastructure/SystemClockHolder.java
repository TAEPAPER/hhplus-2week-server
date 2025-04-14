package kr.hhplus.be.server.infrastructure;

import java.time.Clock;
import kr.hhplus.be.server.application.common.ClockHolder;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
}
