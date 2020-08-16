package crocker.golf.bestball.core.util;

import java.time.Year;
import java.time.ZoneId;

public class TimeHelper {

    private static final String TIME_ZONE = "America/Chicago";

    public static int getCurrentSeason() {
        return Year.now(ZoneId.of(TIME_ZONE)).getValue();
    }
}
