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

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

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
    private static Parent scene;
    private static int userId;
    boolean error;
    String label;
    private ObservableList<String> customerNames = FXCollections.observableArrayList();
    private ObservableList<String> contactNames = FXCollections.observableArrayList();
    private ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();

    private  ObservableList<User> allUsers = FXCollections.observableArrayList();

    private ObservableList<String> userNames = FXCollections.observableArrayList();

    public static void setUserId(int userId) {
        AddAppointmentController.userId = userId;
    }

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
            ObservableList<LocalDateTime> startDateTimes = Appointments.getAllValidStartTimes(date.getValue());

            int i = 0;
            while (i < startDateTimes.size()){
                startTimes.add(startDateTimes.get(i).toLocalTime());
                ++i;
            }

            if(startTimes.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("There are no appointments available on the date selected");
                    alert.showAndWait();
                    return;
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

            ObservableList<LocalDateTime> endDateTimes = Appointments.getAllValidEndTimes(date.getValue(), startTime.getValue());

            int i = 0;
            while (i < endDateTimes.size()){
                endTimes.add(endDateTimes.get(i).toLocalTime());
                ++i;
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
     * cancelling loads the Main Screen
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
     * Adding an appointment checks for appointment conflicts again, saves appointment to the db, and loads the main screen
     * @param actionEvent
     */
    public void onAdd(ActionEvent actionEvent) {
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
            AddAppointmentController.setUserId(Users.getUserByName(employee.getValue().toString()).getUserId());
        }
        LocalDateTime startDateTime = LocalDateTime.of(date.getValue(), startTime.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(date.getValue(), endTime.getValue());

        Appointment appointment = new Appointment(
                Appointments.getId(),
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
            employee.valueProperty().set(null);
            date.setValue(null);
        }
    }

    /**
     * method loads preset values into all applicable fields and uses a lambda function to ensure dates don't conflict with the local business days of.
     * Justification: I need to ensure the available dates in the date picker are within the M-F business days of the business and user
     * the user's region
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int i = 0;
        customerNames.clear();
        ObservableList<Customer> customers = Customers.getAllCustomers();
        while(i < customers.size()){
            String customerName = customers.get(i).getName();
            customerNames.add(i,customerName);
            i++;
        }
        customer.setItems(customerNames);

        i = 0;
        contactNames.clear();
        ObservableList<Contact> contacts = Contacts.getAllContacts();
        while(i < contacts.size()){
            String contactName = contacts.get(i).getContactName();
            contactNames.add(i,contactName);
            i++;
        }
        contact.setItems(contactNames);

        i = 0;
        userNames.clear();
        allUsers = Users.getAllUsers();
        while(i < allUsers.size()){
            String userName = allUsers.get(i).getUsername();
            userNames.add(i,userName);
            i++;
        }
        employee.setItems(userNames);

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
