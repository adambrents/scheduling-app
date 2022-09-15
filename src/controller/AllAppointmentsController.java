package controller;

import database.Appointments;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class AllAppointmentsController implements Initializable {
    @FXML
    private TableView Table;
    @FXML
    private TableColumn contact;
    @FXML
    private TableColumn customer;
    @FXML
    private TableColumn description;
    @FXML
    private TableColumn end;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn location;
    @FXML
    private TableColumn start;
    @FXML
    private TableColumn startDate;
    @FXML
    private TableColumn title;
    @FXML
    private TableColumn type;
    @FXML
    private TableColumn user;
    private int userId;

    private Parent scene;

    private Appointment selectedAppointment = null;

    /**
     * Sets userId
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Loads the add appointment screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onAdd(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointmentsScreen.fxml"));
        scene = loader.load();
        AddAppointmentController controller = loader.getController();
        controller.setUserId(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Warns user of deleting the appointment, then deletes the appointment from the database
     * @param event
     */
    @FXML
    void onDelete(ActionEvent event) {
        selectedAppointment = ((Appointment) Table.getSelectionModel().getSelectedItem());
        if (selectedAppointment == null) {
            return;
        }
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setHeaderText("Delete");
        alert1.setContentText("Are you sure you want to delete Appointment_ID: " + selectedAppointment.getAppointmentID() + ", Type: " + selectedAppointment.getType() + "?");
        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == ButtonType.OK) {
            Appointments.deleteAppointment(selectedAppointment);
            Table.setItems(Appointments.getAllAppointments());
        }
        else {
            alert1.close();
        }

    }

    /**
     * loads the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void onHome(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When user clicks the modify button, the modify appointment screen will load
     * @param actionEvent
     * @throws IOException
     */
    public void onModify(ActionEvent actionEvent) throws IOException {
        if (Table.getSelectionModel() == null) {
            return;
        }
        else {
            selectedAppointment = ((Appointment) Table.getSelectionModel().getSelectedItem());
        }
        if (selectedAppointment.getStart().isBefore(LocalDateTime.now())){
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
        controller.setUserId(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * loads all appointments into table for user to view
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        user.setCellValueFactory(new PropertyValueFactory<>("userID"));

        Table.setItems(Appointments.getAllAppointments());
    }

}
