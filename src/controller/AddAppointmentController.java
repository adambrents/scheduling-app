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
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    public void setUser(int userId) {
        this.userId = userId;
    }
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
    public void onAdd(ActionEvent actionEvent) {
        isApptValid = true;
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

        while (isApptValid){
            if(zonedStartDateTimeEST.isBefore(ZonedESTNOW)){
                errorText.setText("The start date/time is set before the current time");
                isApptValid = false;
            }
            if(zonedEndDateTimeEST.toLocalTime().isAfter(LocalTime.of(22, 0))){
                errorText.setText("The end time is set after the closing time");
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,0))){
                errorText.setText("The selected start time is before business hours");
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isAfter(zonedEndDateTimeEST)){
                errorText.setText("The start time is set after the end Time");
                isApptValid = false;
            }
            if(zonedStartDateTimeEST.isEqual(zonedEndDateTimeEST)){
                errorText.setText("You cannot have an equal start and end time");
                isApptValid = false;
            }
            break;
        }
        error = false;
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
        if (isApptValid){
            LocalDateTime localDateTime = zonedStartDateTimeEST.toLocalDateTime();
            LocalDateTime localDateTime1 = zonedEndDateTimeEST.toLocalDateTime();

            Timestamp start = Timestamp.valueOf(localDateTime);
            Timestamp end = Timestamp.valueOf(localDateTime1);

            Appointment appointment = new Appointment(Appointments.getId(),title.getText(),description.getText(),location.getText(),type.getText(),start,end,Customers.getCustomerId(customer.getValue().toString()),userId,
                    Contacts.getContactID(contact.getValue().toString()),date.getValue(),localDateTime.toLocalTime(),localDateTime1.toLocalTime(), Appointments.getDivisionsAppointments(Appointments.getId()));
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
        int index = 0;
        startTimes.clear();
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
}
