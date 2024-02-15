package repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Times {
    public static int officeClosureReason = 0;
    /**
     * gets the current time in UTC from the database
     * @return
     */
    public static java.sql.Timestamp getTimeStamp(){
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        java.sql.Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp;
    }
}
