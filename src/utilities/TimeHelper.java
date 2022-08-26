package utilities;

import database.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelper {

    private static ObservableList<LocalDateTime> startAvailableTimes = FXCollections.observableArrayList();
    private static ObservableList<LocalDateTime> endAvailableTimes = FXCollections.observableArrayList();

    /**
     * returns a list of available times with taken times removed - doesn't work
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableTimes(ObservableList<LocalDateTime> localDateTimes){
        startAvailableTimes.clear();
        endAvailableTimes.clear();
        LocalDateTime localDateTimeStart = LocalDateTime.of(LocalDate.now(),LocalTime.of(00,00));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59));

        while (localDateTimeStart.isBefore(localDateTimeEnd)){
            startAvailableTimes.add(localDateTimeStart);
            localDateTimeStart = localDateTimeStart.plusMinutes(30);
        }
        int index = 0;
        while (index < localDateTimes.size()){
            LocalDateTime localDateTime = localDateTimes.get(index);
            LocalTime localTime = localDateTime.toLocalTime();
            startAvailableTimes.remove(localTime);
            index++;
        }
        startAvailableTimes.remove(localDateTimes);
        return startAvailableTimes;
    }

    /**
     * gets a list of times from 12am-12am in 30 minute intervals
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableTimes(){
        startAvailableTimes.clear();
        endAvailableTimes.clear();
        LocalDateTime localDateTimeStart = LocalDateTime.of(LocalDate.now(),LocalTime.of(00,00));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59));

        while (localDateTimeStart.isBefore(localDateTimeEnd)){
            startAvailableTimes.add(localDateTimeStart);
            endAvailableTimes.add(localDateTimeStart);
            localDateTimeStart = localDateTimeStart.plusMinutes(30);
        }
        return startAvailableTimes;
    }

    /**
     * converts UTC to the user's system time
     * @param date
     * @return
     */
    public static Date UTCtoLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new  Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    /**
     * returns the EndAvailableTimes list
     *
     * @return
     */
    public static ObservableList<LocalDateTime> getEndAvailableTimes(){
        return endAvailableTimes;
    }

}
