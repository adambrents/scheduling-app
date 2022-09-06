package utilities;

import database.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import model.Appointment;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeHelper {
    private static ObservableList<LocalDateTime> startAvailableTimes = FXCollections.observableArrayList();

    /**
     * accepts a localdate and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param localDate
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableTimes(LocalDate localDate){
        startAvailableTimes.clear();
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDate,LocalTime.of(00,00));
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDate,LocalTime.of(23,59));

        while (localDateTimeStart.isBefore(localDateTimeEnd)){
            startAvailableTimes.add(localDateTimeStart);
            localDateTimeStart = localDateTimeStart.plusMinutes(30);
        }
        return startAvailableTimes;
    }

    /**
     * accepts a localdate and time and appends each available start and end time and returns a list of datetimes in 30 minute intervals
     * @param localDate
     * @param localTime
     * @return
     */
    public static ObservableList<LocalDateTime> getAvailableTimes(LocalDate localDate, LocalTime localTime){
        startAvailableTimes.clear();
        LocalDateTime localDateTimeStart = LocalDateTime.of(localDate,localTime);
        LocalDateTime localDateTimeEnd = LocalDateTime.of(localDate,LocalTime.of(23,59));

        while (localDateTimeStart.isBefore(localDateTimeEnd)){
            startAvailableTimes.add(localDateTimeStart);
            localDateTimeStart = localDateTimeStart.plusMinutes(30);
        }
        return startAvailableTimes;
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


}
