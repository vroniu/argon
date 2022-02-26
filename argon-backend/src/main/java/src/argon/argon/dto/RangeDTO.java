package src.argon.argon.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class RangeDTO implements Serializable {
    private ZonedDateTime rangeStart;
    private ZonedDateTime rangeEnd;

    public ZonedDateTime getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(ZonedDateTime rangeStart) {
        this.rangeStart = rangeStart;
    }

    public ZonedDateTime getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(ZonedDateTime rangeEnd) {
        this.rangeEnd = rangeEnd;
    }
}
