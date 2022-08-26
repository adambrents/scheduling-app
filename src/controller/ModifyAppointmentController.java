package controller;

import database.Appointments;
import database.Contacts;
import database.Customers;
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

public class ModifyAppointmentController implements Initializable {
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
    @FXML
    private Button update;
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private Parent scene;
    private Appointment selectedAppointment;
    private int userId;
    private String label;
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    private int appointmentId;

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

    /**
     * @param selectedAppointment
     */
    public void setAppointment(Appointment selectedAppointment) {
        int i = 0;
        while(i < Customers.getAllCustomers().size()){
            ObservableList<Customer> customers = Customers.getAllCustomers();
            String customerName = customers.get(i).getName();
            customerNames.add(i,customerName);
            i++;
        }
        customer.setValue(Customers.getCustomerName(selectedAppointment.getCustomerID()));
        contact.setValue(Contacts.getContactName(selectedAppointment.getContactID()));
        this.selectedAppointment = selectedAppointment;
        title.setText(selectedAppointment.getTitle());
        description.setText(selectedAppointment.getDescription());
        type.setText(selectedAppointment.getType());
        startTime.setValue(TimeHelper.UTCtoLocalDate(selectedAppointment.getStart()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        endTime.setValue(TimeHelper.UTCtoLocalDate(selectedAppointment.getEnd()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
        date.setValue(selectedAppointment.getStartDate());
        location.setText(selectedAppointment.getLocation());
        appointmentId = selectedAppointment.getAppointmentID();

        //checks to see if the selected date is before current date
        startTimes.clear();
        int index = 0;
        while (index < TimeHelper.getAvailableTimes().size()){
            LocalDateTime localDateTime = TimeHelper.getAvailableTimes().get(index);
            LocalTime localTime = localDateTime.toLocalTime();
            startTimes.add(localTime);
            index++;
        }
        int index2 = 0;
        endTimes.clear();
        while (index2 < TimeHelper.getEndAvailableTimes().size()){
            LocalDateTime localDateTime = TimeHelper.getEndAvailableTimes().get(index2);
            LocalTime localTime = localDateTime.toLocalTime();
            endTimes.add(localTime);
            index2++;
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

    /**
     * @param actionEvent
     * @throws IOException
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * @param userId
     */
    public void setUser(int userId) {
        this.userId = userId;
    }

    /**
     * @param actionEvent
     */
    public void onUpdate(ActionEvent actionEvent) {
        boolean validAppointment = true;
        errorText.setText("");
        LocalDate dateSelected = date.getValue();
        LocalTime startTimeSelected = (LocalTime) startTime.getValue();
        LocalTime endTimeSelected = (LocalTime) endTime.getValue();

        LocalDateTime startDateTime = LocalDateTime.of(dateSelected,startTimeSelected);
        LocalDateTime endDateTime = LocalDateTime.of(dateSelected,endTimeSelected);

        ZonedDateTime zonedStartDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedStartDateTimeEST = zonedStartDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime zonedEndDateTime = endDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedEndDateTimeEST = zonedEndDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime ZonedESTNOW = ZonedDateTime.now(ZoneId.of("America/New_York"));

        while (validAppointment){
            if(zonedStartDateTimeEST.isBefore(ZonedESTNOW)){
                errorText.setText("The start date/time is set before the current time");
                validAppointment = false;
            }
            if(zonedEndDateTimeEST.toLocalTime().isAfter(LocalTime.of(22, 0))){
                errorText.setText("The end time is set after the closing time");
                validAppointment = false;
            }
            if(zonedStartDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,0))){
                errorText.setText("The selected start time is before business hours");
                validAppointment = false;
            }
            if(zonedStartDateTimeEST.isAfter(zonedEndDateTimeEST)){
                errorText.setText("The start time is set after the end Time");
                validAppointment = false;
            }
            if(zonedStartDateTimeEST.isEqual(zonedEndDateTimeEST)){
                errorText.setText("You cannot have an equal start and end time");
                validAppointment = false;
            }
            break;
        }
        boolean error = false;
        if (customer.getValue() == null || contact.getValue() == null || startTime.getValue() == null || endTime.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Combo Box Error");
            alert.setContentText("All combo boxes MUST have something selected");
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
        if(description.getText().length() <1){
            error = true;
            label = "Description";
        }
        if(type.getText().length() < 1){
            error = true;
            label = "Type";
        }
        if(location.getText().length() <1){
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
        if (validAppointment){
            LocalDateTime localDateTime = zonedStartDateTimeEST.toLocalDateTime();
            LocalDateTime localDateTime1 = zonedEndDateTimeEST.toLocalDateTime();

            Timestamp start = Timestamp.valueOf(localDateTime);
            Timestamp end = Timestamp.valueOf(localDateTime1);

            Appointment appointment = new Appointment(appointmentId,title.getText(),description.getText(),location.getText(),type.getText(),start,end,Customers.getCustomerId(customer.getValue().toString()),userId,
                    Contacts.getContactID(contact.getValue().toString()),date.getValue(),localDateTime.toLocalTime(),localDateTime1.toLocalTime(), "", contact.getValue().toString());
            if (Appointments.modifyAppointment(appointment)){
                errorText.setText("Successfully updated appointment :)");
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

    }
}
