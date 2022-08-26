package controller;

import database.Appointments;
import database.Contacts;
import database.Customers;
import database.Times;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import utilities.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    @FXML
    private ComboBox contact;
    @FXML
    private ComboBox customer;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox endTime;
    @FXML
    private Label errorText;
    @FXML
    private TextField location;
    @FXML
    private ComboBox startTime;
    @FXML
    private TextField title;
    @FXML
    private TextField type;
    private static Parent scene;
    private static boolean isApptValid;
    private static int userId;
    boolean error;
    String label;
    private int apptSaveIssue;
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<LocalDateTime> startDateTimes = FXCollections.observableArrayList();
    private ObservableList<LocalDateTime> endDateTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    @FXML
    void onDate(ActionEvent event) throws IOException{

        if(date.getValue() == null){

        }
        else{

            int index = 0;
            startTimes.clear();
            while (index < Appointments.getAllStartTimesByDate(date.getValue()).size() && index < Appointments.getAllEndTimesByDate(date.getValue()).size()){
                LocalDateTime localStartDateTime = Appointments.getAllStartTimesByDate(date.getValue()).get(index);
                LocalDateTime localEndDateTime = Appointments.getAllEndTimesByDate(date.getValue()).get(index);

                if(Appointments.validTimes(localStartDateTime, localEndDateTime)){
                    LocalTime localStartTime = localStartDateTime.toLocalTime();
                    startTimes.add(localStartTime);
                }
                index++;
            }
            if(startTimes.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("There are no appointments on the date selected");
                    alert.showAndWait();
                    return;
            }

            startTime.setItems(startTimes);
        }
    }
    @FXML
    void onMouseClickEnd(InputEvent event) throws IOException{
        if(date.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a date to see end times");
            alert.showAndWait();
            return;
        }
        else if(startTime.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a start time to see end times");
            alert.showAndWait();
            return;
        }
        else{

            int index = 0;
            endTimes.clear();
            while (index < Appointments.getAllEndTimesByDate(date.getValue()).size()){
                LocalDateTime localStartDateTime = LocalDateTime.of(date.getValue(),(LocalTime) startTime.getValue());
                LocalDateTime localEndDateTime = Appointments.getAllEndTimesByDate(date.getValue()).get(index);

                if(!Appointments.validTimes(localStartDateTime, localEndDateTime)){

                }
                else if ((localStartDateTime.isAfter(Appointments.getAllEndTimesByDate(date.getValue()).get(index))
                        ||localStartDateTime.isEqual(Appointments.getAllEndTimesByDate(date.getValue()).get(index)))
                        && Appointments.validTimes(localStartDateTime, localEndDateTime)){

                }
                else {
                    LocalTime localEndTime = localEndDateTime.toLocalTime();
                    endTimes.add(localEndTime);
                }
                index++;
            }
        }
    }
    @FXML
    void onMouseClickStart(InputEvent event) throws IOException{
        if(date.getValue() == null){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must enter a date to see start times");
            alert.showAndWait();
            return;

        }
        else{

        }
    }

    /**
     * @param userId
     */
    public void setUser(int userId) {
        this.userId = userId;
    }

    /**
     * @param event
     * @throws IOException
     */
    @FXML
    void onCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * @param actionEvent
     */
    public void onAdd(ActionEvent actionEvent) {
        if((title.getText() == null) || (description.getText() == null) || (type.getText() == null) || (date.getValue() == null)
                || (startTime.getValue() == null) ||  (endTime.getValue() == null) || (location.getText() == null) || (customer.getValue() == null)
        ||(contact.getValue() == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must provide a value for every field");
            alert.showAndWait();
            return;
        }
        if (title.getText().length() > 50 || description.getText().length() > 50 || type.getText().length() > 50 || location.getText().length() > 50) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Text Box error");
            alert.setContentText("All text boxes must have less than 50 characters");
            alert.showAndWait();
            return;
        }
        if(title.getText().length() < 1){
            error = true;
            label = "Title";
        }
        if(description.getText().length() < 1){
            error = true;
            label = "Description";
        }
        if(type.getText().length() < 1){
            error = true;
            label = "Type";
        }
        if(location.getText().length() < 1){
            error = true;
            label = "Location";
        }
        if(error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(label + " Field Error");
            alert.setContentText(label + " field must not be empty");
            alert.showAndWait();
            return;
        }
        error = false;
        isApptValid = true;
        errorText.setText("");

        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(),(LocalTime) startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(),(LocalTime) endTime.getValue());

        ZonedDateTime zonedStartDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedStartDateTimeEST = zonedStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime zonedEndDateTime = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedEndDateTimeEST = zonedEndDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        while (isApptValid){
            if(zonedEndDateTimeEST.toLocalTime().isAfter(LocalTime.of(22, 0))){
                apptSaveIssue = 1;
                errorText.setText(Times.errorText(apptSaveIssue));
                isApptValid = false;
            }
            if(zonedStartDateTime.toLocalTime().isBefore(LocalTime.of(8,0))){
                apptSaveIssue = 2;
                errorText.setText(Times.errorText(apptSaveIssue));
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isBefore(ZonedESTNOW)){
                apptSaveIssue = 5;
                errorText.setText(Times.errorText(apptSaveIssue));
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isAfter(zonedEndDateTimeEST)){
                apptSaveIssue = 3;
                errorText.setText(Times.errorText(apptSaveIssue));
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isEqual(zonedEndDateTimeEST)){
                apptSaveIssue = 4;
                errorText.setText(Times.errorText(apptSaveIssue));
                isApptValid = false;
            }
            break;
        }
        if (isApptValid){
//            LocalDateTime localDateTime = zonedStartDateTimeEST.toLocalDateTime();
//            LocalDateTime localDateTime1 = zonedEndDateTimeEST.toLocalDateTime();

            Timestamp start = Timestamp.valueOf(zonedStartDateTime.toLocalDateTime());
            Timestamp end = Timestamp.valueOf(zonedEndDateTime.toLocalDateTime());

            Appointment appointment = new Appointment(
                    Appointments.getId(),
                    title.getText(),
                    description.getText(),
                    location.getText(),
                    type.getText(),
                    start,
                    end,
                    Customers.getCustomerId(customer.getValue().toString()),
                    userId,
                    Contacts.getContactID(contact.getValue().toString()),
                    date.getValue(),
                    zonedStartDateTime.toLocalTime(),
                    zonedEndDateTime.toLocalTime(),
                    Appointments.getDivisionsAppointments(Appointments.getId()),
                    contact.getValue().toString());
            if (Appointments.addAppointment(appointment)){
                errorText.setText("Successfully added appointment :)");
                title.clear();
                description.clear();
                location.clear();
                type.clear();
                startTime.valueProperty().set(null);
                endTime.valueProperty().set(null);
                customer.valueProperty().set(null);
                contact.valueProperty().set(null);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Appointment");
                alert.setContentText("Please check the appointment table to make sure appointments do not collide");
                alert.showAndWait();
            }
        }
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 0;
        customerNames.clear();
        while(i < Customers.getAllCustomers().size()){
            ObservableList<Customer> customers = Customers.getAllCustomers();
            String customerName = customers.get(i).getName();
            customerNames.add(i,customerName);
            i++;
        }

        customer.setItems(customerNames);
        contact.setItems(Contacts.getAllContacts());
        startTime.setItems(startTimes);
        endTime.setItems(endTimes);

        //Lambda function - ensures the dates available do not conflict with local business days based on the user's region
        date.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date,empty);
                setDisable(empty || date.compareTo(ZonedDateTime.now(ZoneId.of("America/New_York")).toLocalDate()) < 0);
            }
        });

    }
}
