package crocker.golf.bestball.core.util;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeHelper {

    private static final ZoneId TIME_ZONE = ZoneId.of("UTC");

    public static int getCurrentSeason() {
        return Year.now(TIME_ZONE).getValue();
    }

    public static LocalDateTime getLocalDateTime(ZonedDateTime date) {
        return date.withZoneSameInstant(TIME_ZONE).toLocalDateTime();
    }

    public static ZonedDateTime getZonedDateTime(LocalDateTime date) {
        return date.atZone(TIME_ZONE);
    }
}
