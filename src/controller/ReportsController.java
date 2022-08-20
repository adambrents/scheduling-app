package controller;

import database.Appointments;
import database.Contacts;
import database.Customers;
import database.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;
import static java.time.Month.*;

public class ReportsController implements Initializable {

    @FXML
    private Label appointmentNumber;
    @FXML
    private ComboBox contactBox;
    @FXML
    private TableColumn customerID;
    @FXML
    private TableColumn description;
    @FXML
    private TableColumn endTime;
    @FXML
    private TableColumn id;
    @FXML
    private ComboBox months;
    @FXML
    private TableColumn startDate;
    @FXML
    private TableColumn startTime;
    @FXML
    private TableColumn division;
    @FXML
    private TableView table;
    @FXML
    private TableColumn title;
    @FXML
    private TableColumn type;
    @FXML
    private ComboBox types;
    @FXML
    private ComboBox years;
    @FXML
    private ComboBox report;

    @FXML
    private ComboBox divisions;

    private ObservableList<String> reportsAvailable = FXCollections.observableArrayList();
    private int userId;
    private Parent scene;
    public ObservableList<String> monthsList = FXCollections.observableArrayList();
    public ObservableList<String> yearsList = FXCollections.observableArrayList();
    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<String> reportsList = FXCollections.observableArrayList();
    private Report reports;


    public void setUser(int userId) {
        this.userId = userId;
    }

    public void onReset(ActionEvent event) throws IOException {
        appointmentNumber.setText("0");
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReportsScreen.fxml"));
        scene = loader.load();
        ReportsController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }
    /**
     * @param actionEvent
     */
    public void onSearch(ActionEvent actionEvent) {
        if((types.getValue() == null) || (years.getValue() == null) || (months.getValue() == null) || (report.getValue() == null)
                || (contactBox.getValue() == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("You must select a value in every combo box");
            alert.showAndWait();
            return;
        }

        String apptType = types.getValue().toString();
        int year = parseInt(years.getValue().toString());
        int yearNumber = 2024;
        while (yearNumber >= 2022){
            if(year == yearNumber){
                break;
            }
            else{
                --yearNumber;
            }
        }

        Month month = DECEMBER;
        int i = 12;
        while (i >= 1){
            if(month.toString().equalsIgnoreCase(months.getValue().toString())){
                break;
            }
            else{
                --i;
                month = month.minus(1);
            }
        }

        LocalDateTime localDateTime = LocalDateTime.of(yearNumber,i,1,1,1);
        int x = Appointments.getMonthTypeNumber(localDateTime,apptType);
        String y = String.valueOf(x);
        appointmentNumber.setText(y);

        String contact = contactBox.getValue().toString();

        if (divisions.getValue() == null){
            appointments = Appointments.getContactAppointments(contact);
        }
        else {
            appointments = Appointments.getDivisionAppointmentsList(divisions.getValue().toString(), contact);
        }

        table.setItems(appointments);

        if (report.getValue().toString().equalsIgnoreCase(reports.getReportName())){
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            division.setCellValueFactory(new PropertyValueFactory<>("division"));
        }//TODO possible implementation - add additional if statements for each report and how table is manipulated accordingly
        else{
            //do nothing in case of else
        }

    }

    public void onHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthsList.clear();
        yearsList.clear();

        try {//TODO Add reports to db and make this a getter call for all reports available
            reports = new Report("Customer Appointment", 1);
            reportsList.add(reports.getReportName());

            report.setItems(reportsList);
        }catch (Exception e){
            System.out.println("Error: " + e);
        }

        monthsList.add("January");monthsList.add("February");monthsList.add("March");monthsList.add("April");monthsList.add("May");monthsList.add("June");monthsList.add("July");monthsList.add("August");monthsList.add("September");monthsList.add("October");monthsList.add("November");monthsList.add("December");
        yearsList.add("2022");yearsList.add("2023");yearsList.add("2024");
        months.setItems(monthsList);
        years.setItems(yearsList);
        try {
            types.setItems(Appointments.getTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactBox.setItems(Contacts.getAllContacts());
        divisions.setItems(Divisions.getAllDivisionNames());
    }
}
