package controller;

import database.Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }
    @FXML
    void onAdd(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointment.fxml"));
        scene = loader.load();
        AddAppointmentController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onDelete(ActionEvent event) {
        selectedAppointment = ((Appointment) Table.getSelectionModel().getSelectedItem());
        if(selectedAppointment != null){
            Appointments.deleteAppointment(selectedAppointment);
            Table.setItems(Appointments.getAllAppointments());
        }

    }
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
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        user.setCellValueFactory(new PropertyValueFactory<>("userID"));

        Table.setItems(Appointments.getAllAppointments());
    }

}
