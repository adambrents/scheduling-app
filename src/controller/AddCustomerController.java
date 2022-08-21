package controller;

import database.Customers;
import database.Divisions;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Division;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    public TableView customersTable;
    public TableColumn customerNameColumn;
    public TableColumn customerAddressColumn;
    public TableColumn customerPhoneColumn;
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
    private int userId;

    /**
     * @param event
     */
    public void onCountry(ActionEvent event){

        String selectedCountry = (String) countryBox.getValue();
        if(Objects.equals(selectedCountry, "U.S")){
            divisionBox.setItems(Divisions.getUnitedStatesDivision());
        }
        else if(Objects.equals(selectedCountry, "UK")){
            divisionBox.setItems(Divisions.getUnitedKingdomDivision());
        }
        else{
            divisionBox.setItems(Divisions.getCanadaDivision());
        }

    }

    /**
     * @param event
     * @throws IOException
     */
    public void onCancel(ActionEvent event) throws IOException {

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
        scene = loader.load();
        MainScreenController controller = loader.getController();
        controller.setUser(userId);
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * Called to add a customer to the database
     * @param event
     */
    public void onAdd(ActionEvent event){
        Division division = null;
        boolean valid = true;
        while (valid) {
            if(nameTxt.getText() == ""){
                errorText.setText("Name field MUST be filled out");
                valid = false;
            }
            else if(addressTxt.getText() == ""){
                errorText.setText("Address field MUST be filled out");
                valid = false;
            }
            else if(postalTxt.getText() == "" && postalTxt.getText().matches("[0-9]{5}")){
                errorText.setText("Postal field MUST be filled out");
                valid = false;
            }
            else if(phoneTxt.getText() == "" && phoneTxt.getText().matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$")){
                errorText.setText("Phone field MUST be filled out");
                valid = false;
            } else if(countryBox.getValue() == null) {
                errorText.setText("Country field MUST be filled out");
                valid = false;
            }
            else if(divisionBox.getValue() == null) {
                errorText.setText("Division field MUST be filled out");
                valid = false;
            }
            else{
                try {
                    Customer customer = new Customer(Customers.getId(),nameTxt.getText(),addressTxt.getText(),postalTxt.getText(),phoneTxt.getText(),countryBox.getValue().toString(),divisionBox.getValue().toString(),Divisions.getDivisionId((String) divisionBox.getValue()));
                    System.out.println(customer);
                    Customers.addCustomer(customer);
                    errorText.setText("Successfully added Customer");
                    customersTable.setItems(Customers.getAllCustomers());
                    valid = false;
                    nameTxt.clear();
                    addressTxt.clear();
                    postalTxt.clear();
                    phoneTxt.clear();
                    countryBox.valueProperty().set(null);
                    divisionBox.valueProperty().set(null);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    valid = false;
                }
            }
        }
    }

    /**
     * @param userId
     * @return
     */
    public int setUser(int userId){
        return this.userId;
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //sets dropdown with all countries where service is available
        countryBox.setItems(Customers.getAllCountries());

        //Get data for Customer Table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        //sets table with customer data
        customersTable.setItems(Customers.getAllCustomers());

    }
}
