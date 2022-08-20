package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;

public class TimeHelper {

    private static ObservableList<LocalDateTime> startAvailableTimes = FXCollections.observableArrayList();
    private static ObservableList<LocalDateTime> endAvailableTimes = FXCollections.observableArrayList();

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

    public static ObservableList<LocalDateTime> getEndAvailableTimes(){
        return endAvailableTimes;
    }

    public static ZonedDateTime getEST(){
        return ZonedDateTime.of(LocalDate.now(),LocalTime.now(),ZoneId.of("America/New_York"));
    }
}
