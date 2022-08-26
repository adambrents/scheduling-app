package database;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Times {
    public static int officeClosureReason = 0;
    /**
     * @return
     */
    public static java.sql.Timestamp getTimeStamp(){
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        java.sql.Timestamp timestamp = Timestamp.valueOf(localDateTime);
        return timestamp;
    }

    public static String errorText(int errorMsgNo){
        String errorMsg = "";
        if(errorMsgNo == 1){
            errorMsg = "The end time is set after the closing time";
            return errorMsg;
        }
        else if(errorMsgNo == 2){
            errorMsg = "The selected start time is before business hours";
            return errorMsg;
        }
        else if(errorMsgNo == 3){
            errorMsg = "The start time is set after the end Time";
            return errorMsg;
        }
        else if(errorMsgNo == 4){
            errorMsg = "You cannot have an equal start and end time";
            return errorMsg;
        }
        else if(errorMsgNo == 5){
            errorMsg = "The start date/time is set before the current time";
            return errorMsg;
        }
         return errorMsg;
    }

}
