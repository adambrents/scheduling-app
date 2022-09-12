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
import model.Contact;
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static java.time.Month.*;

public class ReportsController implements Initializable {

    @FXML
    private Label appointmentNumber;
    @FXML
    private Label totalAppointments;
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
    private ObservableList<Report> reports = FXCollections.observableArrayList();
    private Report addReport;

    /**
     * sets UserId
     * @param userId
     */
    public void setUser(int userId) {
        this.userId = userId;
    }

    /**
     * Reloads the screen
     * @param event
     * @throws IOException
     */
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
     * Based on user selection, a report is run and results are shown on the right side of the filters
     * @param actionEvent
     */
    public void onSearch(ActionEvent actionEvent) {
        if (report.getValue().toString().equalsIgnoreCase(reports.get(0).getReportName())) {
            if((types.getValue() == null) || (years.getValue() == null) || (months.getValue() == null) || (report.getValue() == null)
                    || (contactBox.getValue() == null)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("You must select a value in every combo box");
                alert.showAndWait();
                return;
            }
            String contact = contactBox.getValue().toString();
            appointments = Appointments.getContactAppointments(contact);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
        }

        if (report.getValue().toString().equalsIgnoreCase(reports.get(1).getReportName())){
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
        }
        if (report.getValue().toString().equalsIgnoreCase(reports.get(2).getReportName())){
            table.setDisable(false);
            if((types.getValue() == null) || (years.getValue() == null) || (months.getValue() == null) || (report.getValue() == null)
                    || (contactBox.getValue() == null) || (divisions.getValue() == null)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("You must select a value in every combo box");
                alert.showAndWait();
                return;
            }
            String contact = contactBox.getValue().toString();
            appointments = Appointments.getDivisionAppointmentsList(divisions.getValue().toString(), contact);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
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

        if (report.getValue().toString().equalsIgnoreCase(reports.get(0).getReportName())){
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            division.setCellValueFactory(new PropertyValueFactory<>("division"));

            table.setItems(appointments);
        }//TODO possible implementation - add additional if statements for each report and how table is manipulated accordingly
        else if (report.getValue().toString().equalsIgnoreCase(reports.get(2).getReportName())) {
            id.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            type.setCellValueFactory(new PropertyValueFactory<>("type"));
            division.setCellValueFactory(new PropertyValueFactory<>("division"));
            table.setItems(appointments);
        }
        else {
            //do nothing in case of else
        }

    }

    /**
     * Loads mainscreen
     * @param actionEvent
     * @throws IOException
     */
    public void onHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Enables, disables, or removes fields that are not applicable based on report chosen
     * @param event
     * @throws IOException
     */
    @FXML
    public void onChooseReport(ActionEvent event) throws IOException{
        if (report.getValue().toString().equalsIgnoreCase(reports.get(0).getReportName())){
            divisions.setDisable(true);
            contactBox.setDisable(false);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
            table.setDisable(false);
        }
        if (report.getValue().toString().equalsIgnoreCase(reports.get(1).getReportName())){
            divisions.setDisable(true);
            contactBox.setDisable(true);
            totalAppointments.setVisible(true);
            appointmentNumber.setVisible(true);
            table.setDisable(true);
        }
        if (report.getValue().toString().equalsIgnoreCase(reports.get(2).getReportName())){
            divisions.setDisable(false);
            contactBox.setDisable(false);
            totalAppointments.setVisible(false);
            appointmentNumber.setVisible(false);
            table.setDisable(false);
        }
    }

    /**
     * loads all preset data into screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthsList.clear();
        yearsList.clear();
        try {//TODO Add reports to db and make this a getter call for all reports available
            addReport = new Report("Appointment Schedule", 1);
            reports.add(addReport);
            addReport = new Report("Total Customer Appointments by Type", 2);
            reports.add(addReport);
            addReport = new Report("Appointment Schedule by Division", 3);
            reports.add(addReport);
            int index = 0;
            while (index < reports.size()){
                reportsList.add(reports.get(index).getReportName());
                index++;
            }
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
        int index = 0;
        ObservableList<Contact> contacts = Contacts.getAllContacts();
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        while (index < contacts.size()){
            String contactName = contacts.get(index).getContactName();
            contactNames.add(contactName);
            ++index;
        }
        contactBox.setItems(contactNames);
        divisions.setItems(Divisions.getAllDivisionNames());
    }

}
