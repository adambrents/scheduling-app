package controller;

import repository.Customers;
import repository.Divisions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import viewmodels.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    public TableColumn customerNameColumn;
    public TableColumn customerAddressColumn;
    public TableColumn customerPhoneColumn;
    public TableView customersTable;
    public Customer selectedCustomer;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField addressTxt;
    public TextField postalTxt;
    public Label division;
    public TextField phoneTxt;
    public ComboBox countryBox;
    public ComboBox divisionBox;
    public Label errorText;
    private Parent scene;
    private int divisionId;
    private int customerId;
    private int userId;

    /**
     * prepopulates dropdown values and populates customer table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Get data for Customer Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        customersTable.setItems(Customers.getAllCustomers());

    }

    /**
     * @param selectedCustomer
     */
    public void setCustomer(Customer selectedCustomer) {
        countryBox.setItems(Customers.getAllCountries());

        this.selectedCustomer = selectedCustomer;
        divisionId = selectedCustomer.getDivisionId();
        customerId = selectedCustomer.getId();
        idTxt.setText(Integer.toString(selectedCustomer.getId()));
        nameTxt.setText(selectedCustomer.getName());
        addressTxt.setText(selectedCustomer.getAddress());
        postalTxt.setText(selectedCustomer.getPostalCode());
        phoneTxt.setText(selectedCustomer.getPhoneNumber());
        countryBox.setValue(selectedCustomer.getCountry());
        divisionBox.setValue(selectedCustomer.getDivision());
        String country = selectedCustomer.getCountry();
        if(Objects.equals(country, "U.S")){
            divisionBox.setItems(Divisions.getUnitedStatesDivision());
        }
        else if(Objects.equals(country, "UK")){
            divisionBox.setItems(Divisions.getUnitedKingdomDivision());
        }
        else{
            divisionBox.setItems(Divisions.getCanadaDivision());
        }


    }
    /**
     * sets divisions based on country when a country value is selected
     * @param actionEvent
     */
    public void onCountry(ActionEvent actionEvent) {
        String countryValue = (String) countryBox.getValue();
        if (Objects.equals(countryValue, "U.S")) {
            divisionBox.setItems(Divisions.getUnitedStatesDivision());
            divisionBox.setValue("Alabama");
            divisionId = 1;
        }
        else if(Objects.equals(countryValue, "UK")){
            divisionBox.setItems(Divisions.getUnitedKingdomDivision());
            divisionBox.setValue("Scotland");
            divisionId = 103;
        }
        else{
            divisionBox.setItems(Divisions.getCanadaDivision());
            divisionBox.setValue("Ontario");
            divisionId = 67;
        }
    }

    /**
     * on cancel loads the main screen
     *
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
     * validates if required fields have values and updates customer in the database
     * @param actionEvent
     */
    public void onUpdate(ActionEvent actionEvent) throws IOException {
        boolean valid = true;
        while (valid){
            if(nameTxt.getText() == ""){
                errorText.setText("Name field MUST be filled out");
                valid = false;
            }
            else if(addressTxt.getText() == ""){
                errorText.setText("Address field MUST be filled out");
                valid = false;
            }
            else if(postalTxt.getText() == ""){
                errorText.setText("Postal field MUST be filled out");
                valid = false;
            }
            else if(phoneTxt.getText() == ""){
                errorText.setText("Phone field MUST be filled out");
                valid = false;
            }
            else {
                try {
                    String divisionName = (String) divisionBox.getValue();
                    this.divisionId = Divisions.getDivisionId(divisionName);
                    Customer customer = new Customer(customerId,nameTxt.getText(),addressTxt.getText(),postalTxt.getText(),phoneTxt.getText(),(String) countryBox.getValue(),(String) divisionBox.getValue(), divisionId);
                    Customers.updateCustomer(customer);
                    customersTable.setItems(Customers.getAllCustomers());
                    valid = false;
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
                    scene = loader.load();
                    MainScreenController controller = loader.getController();
                    controller.setUser(userId);
                    stage.setScene(new Scene(scene));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    valid = false;
                }
            }
        }
    }

    /**
     * sets the userId
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}