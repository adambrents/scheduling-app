package utilities;

import database.Times;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelper {
    private static ObservableList<LocalDateTime> availableTimes = FXCollections.observableArrayList();

    /**
     * accepts a localdate and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param localDate
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableStartTimes(LocalDate localDate){
        availableTimes.clear();

        LocalDateTime localDateTimeStart = LocalDateTime.of(localDate,LocalTime.of(00,00));
        ZonedDateTime startZonedDateTime = ZonedDateTime.of(localDateTimeStart, ZoneId.systemDefault());

        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDateTimeStart.toLocalDate(),LocalTime.of(23,59));
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(localDateTimeEnd, ZoneId.systemDefault());

        while (startZonedDateTime.isBefore(endZonedDateTime)){
            if(validStartBusinessHours(startZonedDateTime.toLocalDateTime())){
                availableTimes.add(startZonedDateTime.toLocalDateTime());
            }
            startZonedDateTime = startZonedDateTime.plusMinutes(30);
        }
        return availableTimes;
    }

    /**
     * accepts a localDateTime and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param endDateTime
     * @param startTime
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableEndTimes(LocalDateTime endDateTime, LocalTime startTime){
        availableTimes.clear();


        LocalDateTime localDateTimeStart = LocalDateTime.of(endDateTime.toLocalDate(),startTime.plusMinutes(30));
        ZonedDateTime startingIncrementZonedDateTime = ZonedDateTime.of(localDateTimeStart, ZoneId.systemDefault());

        LocalDateTime localDateTimeEnd = LocalDateTime.of(endDateTime.toLocalDate(),LocalTime.of(22,1));
        ZonedDateTime endZonedDateTime = ZonedDateTime.of(localDateTimeEnd, ZoneId.of("America/New_York"));

        while (startingIncrementZonedDateTime.isBefore(endZonedDateTime)){
            if(validEndBusinessHours(startingIncrementZonedDateTime, endZonedDateTime)){
                availableTimes.add(startingIncrementZonedDateTime.toLocalDateTime());
            }
            startingIncrementZonedDateTime = startingIncrementZonedDateTime.plusMinutes(30);
        }
        return availableTimes;
    }
    /**
     * method to contain datepicker lambda that disables any dates prior to today's date
     */
    public static void datePickerDisablePastDays(){
        DatePicker dp = new DatePicker();
        dp.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
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

    public static boolean validEndBusinessHours(ZonedDateTime startDateTime, ZonedDateTime endDateTime){

        boolean isApptValid = true;

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        while (isApptValid){
            if(endDateTime.toLocalTime().isBefore(LocalTime.of(8,0))){
                isApptValid = false;
            }
            if(endDateTime.toLocalTime().isAfter(LocalTime.of(22, 1))){
                isApptValid = false;
            }
            if(endDateTime.isBefore(ZonedESTNOW)){
                isApptValid = false;
            }
            if(endDateTime.toLocalTime().equals(startDateTime.toLocalTime())){
                isApptValid = false;
            }
            break;
        }
        return isApptValid;
    }
    public static boolean validStartBusinessHours(LocalDateTime startDateTime){

        boolean isApptValid = true;

        ZonedDateTime zoneStartDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedStartDateTimeEST = zoneStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        while (isApptValid){
            if(zonedStartDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,0))){
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.toLocalTime().isAfter(LocalTime.of(21, 30))){
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isBefore(ZonedESTNOW)){
                isApptValid = false;
            }
            break;
        }
        return isApptValid;
    }
}
