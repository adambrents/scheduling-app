package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utilities.JDBCConnectionHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customers {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<String> allCountries = FXCollections.observableArrayList();
    private static int lastID = 0;
    private static Statement statement;

    public static ObservableList<String> getAllCountries() {
        try{
            allCountries.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Country "
                         + "FROM client_schedule.countries;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                allCountries.add(resultSet.getString("Country"));
            }
            return allCountries;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<Customer> getAllCustomers() {
        try{
            allCustomers.clear();
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Countries.Country, "
                         + "first_level_divisions.Division, "
                         + "Customers.Customer_ID, "
                         + "Customers.Customer_Name, "
                         + "Customers.Address, "
                         + "Customers.Postal_Code, "
                         + "Customers.Phone, "
                         + "Customers.Division_ID "
                         + "FROM client_schedule.customers "
                         + "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID "
                         + "INNER JOIN Countries ON first_level_divisions.Country_ID = Countries.Country_ID "
                         + "WHERE Customer_Name IS NOT NULL;";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                Customer customer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Country"),
                        resultSet.getString("Division"),
                        resultSet.getInt("Division_ID"));
                if (customer.getName() == ""){
                    //do nothing if the customer does not have a name
                    //This is used for when customers are "deleted"
                    //Customers ids will still exist within the database so no id is made more than once
                    //all other customer data will be removed from the database
                }
                else {
                    allCustomers.add(customer);
                }
            }
            statement.close();
            return allCustomers;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void updateCustomer(Customer customer) {
        try {
            PreparedStatement statement = JDBC.getConnection().prepareStatement(
                    "UPDATE client_schedule.customers "
                      + "SET Customer_Name = '" + customer.getName()
                      + "', Address = '" + customer.getAddress()
                      + "', Phone = '" + customer.getPhoneNumber()
                      + "', Postal_Code = '" + customer.getPostalCode()
                      + "', Division_ID = '" + customer.getDivisionId()
                      + "' WHERE customers.Customer_ID = " + customer.getId() + ";");
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
        }
    }

    public static int getId() {
        try {
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Customer_ID "
                         + "FROM client_schedule.customers "
                         + "ORDER BY Customer_ID;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt(1) > lastID) {
                    lastID = resultSet.getInt(1);
                } else {
                    //do nothing if the id is less than last id
                }
            }
            lastID++;
            statement.close();

            return lastID;
        } catch (SQLException sqlException) {
            return -1;
        }
    }

    public static void addCustomer(Customer customer) {
        try {
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(
                    "INSERT INTO customers " +
                        "VALUES(" + customer.getId() +
                            ", '" + customer.getName() +
                           "', '" + customer.getAddress() +
                           "', '" + customer.getPostalCode() +
                           "', '" + customer.getPhoneNumber() +
                           "', "  + "NULL, "
                                  + "NULL, "
                                  + "NULL, "
                                  + "NULL, "
                                  + customer.getDivisionId() + ");");
            preparedStatement.executeUpdate();
        }
        catch (SQLException sqlException){
            System.out.println(sqlException);
        }
    }

    public static String getCustomerName(int customerID){
        try{
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Customer_Name "
                         + "FROM client_schedule.customers "
                         + "WHERE Customer_ID = " + customerID + ";";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String customerName = resultSet.getString(1);
                return customerName;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static int getCustomerId(String customerName){
        try{
            Statement statement = JDBC.getConnection().createStatement();
            String query = "SELECT Customer_ID FROM client_schedule.customers WHERE Customer_Name ='" + customerName + "';";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int customerID = resultSet.getInt(1);
                return customerID;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteCustomer(Customer selectedCustomer){
        try{PreparedStatement statement = JDBC.getConnection().prepareStatement(
                "UPDATE client_schedule.customers "
                  + "SET "
                  + "Customer_Name=NULL, "
                  + "Address=NULL, "
                  + "Postal_Code=NULL, "
                  + "Phone=NULL, "
                  + "Create_Date=NULL, "
                  + "Created_By=NULL,"
                  + "Last_Update=NULL, "
                  + "Last_Updated_By=NULL "
                  + "WHERE Customer_ID = " + selectedCustomer.getId() + ";");
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
