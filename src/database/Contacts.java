package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.JDBCConnectionHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Contacts {
    private static ObservableList<String> allContacts = FXCollections.observableArrayList();
    private static Statement statement;

    /**
     * gets a list of all contact names
     * @return
     */
    public static ObservableList<String> getAllContacts(){
        allContacts.clear();
        try{
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT * FROM client_schedule.contacts;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String contactName = resultSet.getString(2);
                allContacts.add(contactName);
            }
            statement.close();
            return allContacts;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets a list of all contact ids based on contact names
     * @param contactName
     * @return
     */
    public static int getContactID(String contactName){
        try{
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Contact_ID FROM client_schedule.contacts WHERE Contact_Name = '" + contactName + "';";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * gets a list of all contact names based on contact ids
     * @param contactID
     * @return
     */
    public static String getContactName(int contactID){
        try{
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Contact_Name FROM client_schedule.contacts WHERE Contact_ID=" + contactID + ";";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
