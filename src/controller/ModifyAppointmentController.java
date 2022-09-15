package controller;

import database.Appointments;
import database.Contacts;
import database.Customers;
import database.Users;
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
import model.Contact;
import model.Customer;
import model.User;
import utilities.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.*;
import java.util.EmptyStackException;
import java.util.ResourceBundle;

public class ModifyAppointmentController implements Initializable {
    @FXML
    private ComboBox contact;
    @FXML
    private ComboBox customer;
    @FXML
    private ComboBox employee;
    @FXML
    private DatePicker date;
    @FXML
    private TextArea description;
    @FXML
    private ComboBox<LocalTime> endTime;
    @FXML
    private Label errorText;
    @FXML
    private TextField location;
    @FXML
    private ComboBox<LocalTime> startTime;
    @FXML
    private TextField title;
    @FXML
    private TextField type;
    @FXML
    private Button update;
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<String> contactNames = FXCollections.observableArrayList();
    private Parent scene;
    private Appointment selectedAppointment;
    private static int userId;
    private String label;
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();
    private int appointmentId;
    private boolean error;
    private  ObservableList<User> allUsers = FXCollections.observableArrayList();

    private ObservableList<String> userNames = FXCollections.observableArrayList();
    /**
     * when a date is selected, start and end times are populated
     * @param event
     * @throws IOException
     */
    @FXML
    void onDate(ActionEvent event) throws IOException{
        startTime.setItems(null);
        endTime.setItems(null);
        startTime.setValue(null);
        endTime.setValue(null);
        startTimes.clear();
        endTimes.clear();
        if(date.getValue() == null){
        }
        else{
            ObservableList<LocalDateTime> startDateTimes = Appointments.getAllValidStartTimes(LocalDate.from(date.getValue()));
            int i = 0;
            while (i < startDateTimes.size()){
                startTimes.add(startDateTimes.get(i).toLocalTime());
                ++i;
            }
            if(startTimes.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("There are no other appointments available on the date selected");
                alert.showAndWait();
            }
            if(startTime.getValue() != null){
                startTimes.add(LocalTime.parse(startTime.getValue().toString()));
            }
            startTime.setItems(startTimes);
        }
    }
    /**
     * when end time is selected, date and start time fields are checked to ensure a value is present
     * if value is present, end times will be added to drop down only if there are no collisions with other appointments
     * if collisions exist, all times equal to and after start times will not be displayed in drop down
     * @param event
     * @throws IOException
     */
    @FXML
    void onMouseClickEnd(InputEvent event) throws IOException{
        endTimes.clear();
        endTime.setItems(null);
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
            ObservableList<LocalDateTime> endDateTimes = Appointments.getAllValidEndTimes(LocalDate.from(date.getValue()), LocalTime.parse(startTime.getValue().toString()));
            int i = 0;
            while (i < endDateTimes.size()){
                endTimes.add(endDateTimes.get(i).toLocalTime());
                ++i;
            }
            if(endTime.getValue() != null){
                endTimes.add(LocalTime.parse(endTime.getValue().toString()));
            }
            endTime.setItems(endTimes);
        }
    }
    /**
     * when start time is selected, date field is checked to ensure a value is present
     * @param event
     * @throws IOException
     */
    @FXML
    void onMouseClickStart(InputEvent event) throws IOException{
        endTime.setValue(null);
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
     * loads all appointment information into the modify appointment screen to be changed and reviewed
     *
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
        customer.setItems(customerNames);
        customer.setValue(Customers.getCustomerName(selectedAppointment.getCustomerID()));
        i = 0;
        while(i < Contacts.getAllContacts().size()){
            ObservableList<Contact> contacts = Contacts.getAllContacts();
            String contactName = contacts.get(i).getContactName();
            contactNames.add(i,contactName);
            i++;
        }
        contact.setItems(contactNames);
        contact.setValue(Contacts.getContactName(selectedAppointment.getContactID()));
        i = 0;
        userNames.clear();
        allUsers = Users.getAllUsers();
        while(i < allUsers.size()){
            String userName = allUsers.get(i).getUsername();
            userNames.add(i,userName);
            i++;
        }
        employee.setItems(userNames);
        employee.setValue(Users.getUserById(selectedAppointment.getUserID()).getUsername());
        this.selectedAppointment = selectedAppointment;
        title.setText(selectedAppointment.getTitle());
        description.setText(selectedAppointment.getDescription());
        type.setText(selectedAppointment.getType());
        startTime.setValue(selectedAppointment.getStartTime());
        endTime.setValue(selectedAppointment.getEndTime());
        date.setValue(selectedAppointment.getStart().toLocalDate());
        location.setText(selectedAppointment.getLocation());
        appointmentId = selectedAppointment.getAppointmentID();
        //checks to see if the selected date is before current date
        int index = 0;
        int validStartTimeCount = Appointments.getAllValidStartTimes(date.getValue()).size();
        startTimes.clear();
        if(startTime.getValue() != null){
            startTimes.add(LocalTime.from(LocalTime.parse(startTime.getValue().toString())));
        }
        while (index < validStartTimeCount){
            LocalDateTime localDateTime = Appointments.getAllValidStartTimes(LocalDate.from(date.getValue())).get(index);
            startTimes.add(localDateTime.toLocalTime());
            index++;
        }
        index = 0;
        int validEndTimeCount = Appointments.getAllValidEndTimes(LocalDate.from(date.getValue()),LocalTime.parse(startTime.getValue().toString())).size();
        endTimes.clear();
        if(endTime.getValue() != null){
            endTimes.add(LocalTime.from(LocalTime.parse(endTime.getValue().toString())));
        }
        while (index < validEndTimeCount){
            LocalDateTime localDateTime = Appointments.getAllValidEndTimes(LocalDate.from(date.getValue()),LocalTime.parse(startTime.getValue().toString())).get(index);
            endTimes.add(localDateTime.toLocalTime());
            index++;
        }
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
     * cancelling loads the Main Screen
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
     * sets userId
     * @param userId
     */
    public static void setUserId(int userId) {
        ModifyAppointmentController.userId = userId;
    }

    /**
     * Updating an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     * @param actionEvent
     */
    public void onUpdate(ActionEvent actionEvent) {
        error = false;
        label = "";
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
        if(employee.getValue() != null){
            ModifyAppointmentController.setUserId(Users.getUserByName(employee.getValue().toString()).getUserId());
        }
        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(), startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(), endTime.getValue());
        Appointment appointment = new Appointment(
                appointmentId,
                title.getText(),
                description.getText(),
                location.getText(),
                type.getText(),
                startDateTime,
                endDateTime,
                Customers.getCustomerId(customer.getValue().toString()),
                userId,
                Contacts.getContactID(contact.getValue().toString()),
                Appointments.getDivisionsAppointments(Appointments.getId()),
                contact.getValue().toString());
            if (Appointments.modifyAppointment(appointment)){
            errorText.setText("Successfully updated appointment :)");
        }
    }
    /**
     * method loads preset values into all applicable fields
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
