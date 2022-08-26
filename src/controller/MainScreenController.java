package controller;

import database.Appointments;
import database.Customers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Button addCustomer;
    @FXML
    private TableColumn contactMonth;
    @FXML
    private TableColumn contactWeek;
    @FXML
    private TableColumn customerAddressColumn;
    @FXML
    private TableColumn customerCountryColumn;
    @FXML
    private TableColumn customerDivisionColumn;
    @FXML
    private TableColumn customerMonth;
    @FXML
    private TableColumn customerNameColumn;
    @FXML
    private TableColumn customerWeek;
    @FXML
    private TableView customersTable;
    @FXML
    private Button deleteCustomer;
    @FXML
    private TableColumn descriptionMonth;
    @FXML
    private TableColumn descriptionWeek;
    @FXML
    private TableColumn endMonth;
    @FXML
    private TableColumn endWeek;
    @FXML
    private Button exit;
    @FXML
    private TableColumn idMonth;
    @FXML
    private TableColumn idWeek;
    @FXML
    private TableColumn locationMonth;
    @FXML
    private TableColumn locationWeek;
    @FXML
    private Button modifyCustomer;
    @FXML
    private TableView monthTable;
    @FXML
    private Button onAddAppointment;
    @FXML
    private Button onDeleteAppointment;
    @FXML
    private Button onModifyAppointment;
    @FXML
    private TableColumn startDate;
    @FXML
    private TableColumn startDate1;
    @FXML
    private TableColumn startMonth;
    @FXML
    private TableColumn startWeek;
    @FXML
    private Tab thisMonthTab;
    @FXML
    private Tab thisWeekTab;
    @FXML
    private TableColumn titleMonth;
    @FXML
    private TableColumn titleWeek;
    @FXML
    private TableColumn typeMonth;
    @FXML
    private TableColumn typeWeek;
    @FXML
    private TableColumn userMonth;
    @FXML
    private TableColumn userWeek;
    @FXML
    private TableView weekTable;
    private Parent scene;
    private Appointment selectedAppointment = null;
    private Customer selectedCustomer = null;
    public int userId;


    /**
     * When the add customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomerScreen.fxml"));
        scene = loader.load();
        AddCustomerController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When the modify customer button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            return;
        }
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyCustomersScreen.fxml"));
        scene = loader.load();
        ModifyCustomerController controller = loader.getController();
        controller.setCustomer(selectedCustomer);
        controller.setUserId(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * When the delete customer button is clicked, validate request with user then proceed with deleting from db(and ui table)
     * Also validates delete eligibility and deletes any associated appointments if applicable
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        selectedCustomer = ((Customer) customersTable.getSelectionModel().getSelectedItem());
        if(selectedCustomer != null) {
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Do you want to delete this customer?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    if(Appointments.checkForAppointments(selectedCustomer)){
                        Customers.deleteCustomer(selectedCustomer);
                        customersTable.setItems(Customers.getAllCustomers());
                        return;
                    }
                    if(!Appointments.checkForAppointments(selectedCustomer)){
                        Appointments.deleteAppointment(selectedCustomer.getId());
                        Customers.deleteCustomer(selectedCustomer);
                        customersTable.setItems(Customers.getAllCustomers());
                        return;
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error");
                        alert.setContentText("Issue deleting customer - see console for details");
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else {
                alert1.close();
            }
        }
    }

    /**
     * init method, sets values for customer and appointment ui tables from db
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Customer Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));

        //sets table with customer data
        customersTable.setItems(Customers.getAllCustomers());

        //Get data for Weekly Appointments table
        idWeek.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleWeek.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionWeek.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationWeek.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactWeek.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeWeek.setCellValueFactory(new PropertyValueFactory<>("type"));
        startWeek.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endWeek.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        customerWeek.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userWeek.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //sets table with weekly appt data
        weekTable.setItems(Appointments.getWeeklyAppointments());

        //Get data for Monthly Appointments table
        idMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleMonth.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionMonth.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationMonth.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactMonth.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeMonth.setCellValueFactory(new PropertyValueFactory<>("type"));
        startMonth.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endMonth.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate1.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        customerMonth.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userMonth.setCellValueFactory(new PropertyValueFactory<>("userID"));

        //sets table with monthly appt data
        monthTable.setItems(Appointments.getMonthlyAppointments());


    }

    /**
     * When the exit button is clicked, exit application
     * @param actionEvent
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    /**
     * When the add appointment button is clicked, load corresponding screen
     * @param actionEvent
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentsScreen.fxml"));
        scene = loader.load();
        AddAppointmentController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When the modify appointment button is clicked, load corresponding screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {
        if (thisWeekTab.isSelected()) {
            selectedAppointment = ((Appointment) weekTable.getSelectionModel().getSelectedItem());
        }
        else if(thisMonthTab.isSelected()) {
            selectedAppointment = ((Appointment) monthTable.getSelectionModel().getSelectedItem());
        }
        if (selectedAppointment == null) {
            return;
        }
        if (selectedAppointment.getStart().before(Timestamp.valueOf(LocalDateTime.now()))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You may not alter an appointment that has already occurred :(");
            alert.showAndWait();
            return;
        }
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyAppointmentsScreen.fxml"));
        scene = loader.load();
        ModifyAppointmentController controller = loader.getController();
        controller.setAppointment(selectedAppointment);
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * when clicked, user is warned of deleting appointment, then appointment is deleted
     *
     * @param actionEvent
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        if (thisWeekTab.isSelected()) {
            selectedAppointment = ((Appointment) weekTable.getSelectionModel().getSelectedItem());
            if (selectedAppointment == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedAppointment.getAppointmentID() + ", Type: " + selectedAppointment.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK) {
                Appointments.deleteAppointment(selectedAppointment);
                weekTable.setItems(Appointments.getWeeklyAppointments());
            }
            else {
                alert1.close();
            }
        } else if (thisMonthTab.isSelected()) {
            selectedAppointment = ((Appointment) monthTable.getSelectionModel().getSelectedItem());
            if (selectedAppointment == null) {
                return;
            }
            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setHeaderText("Delete");
            alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedAppointment.getAppointmentID() + ", Type: " + selectedAppointment.getType() + "?");
            Optional<ButtonType> result = alert1.showAndWait();
            if (result.get() == ButtonType.OK){
                Appointments.deleteAppointment(selectedAppointment);
                monthTable.setItems(Appointments.getMonthlyAppointments());
            }
            else {
                alert1.close();
            }
        }
    }


    /**
     * sets userId
     * @param user
     */
    public void setUser(int user) {
        userId = user;
    }

    /**
     * Loads reports screen
     * @param actionEvent
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        scene = loader.load();
        ReportsController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * loads allappointments screen
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onAllAppointments(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AllAppointmentsScreen.fxml"));
        scene = loader.load();
        AllAppointmentsController controller = loader.getController();
        controller.setUserId(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
