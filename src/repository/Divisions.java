package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.JDBCConnectionHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Divisions {

    private static ObservableList<String> unitedStatesDivisions = FXCollections.observableArrayList();
    private static ObservableList<String> canadaDivisions = FXCollections.observableArrayList();
    private static ObservableList<String> unitedKingdomDivisions = FXCollections.observableArrayList();

    private static ObservableList<String> allDivisionNames = FXCollections.observableArrayList();

    private static Statement statement;
    private static int divisionId = 0;
    private static String divisionName;

    /**
     * gets a list of divisions in the US
     * @return
     */
    public static ObservableList<String> getUnitedStatesDivision() {
        try {
            unitedStatesDivisions.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Division FROM client_schedule.first_level_divisions where Country_ID = 1;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String divisionName = resultSet.getString("Division");
                unitedStatesDivisions.add(divisionName);
            }
             return unitedStatesDivisions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets a list of divisions in canada
     *
     * @return
     */
    public static ObservableList<String> getCanadaDivision() {
        try {
            canadaDivisions.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Division FROM client_schedule.first_level_divisions where Country_ID = 3;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String divisionName = resultSet.getString("Division");
                canadaDivisions.add(divisionName);
            }
            return canadaDivisions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets a list of divisions in the UK
     *
     * @return
     */
    public static ObservableList<String> getUnitedKingdomDivision() {
        try {
            unitedKingdomDivisions.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Division FROM client_schedule.first_level_divisions where Country_ID = 2;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String divisionName = resultSet.getString("Division");
                unitedKingdomDivisions.add(divisionName);
            }
            return unitedKingdomDivisions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * gets the division ID based on the division name
     * @param divisionName
     * @return
     */
    public static int getDivisionId(String divisionName) {
        try {
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Division_ID FROM client_schedule.first_level_divisions WHERE Division='" + divisionName + "';";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                divisionId = resultSet.getInt(1);
            }
            return divisionId;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * returns a list of all division names
     *
     * @return
     */
    public static ObservableList<String> getAllDivisionNames() {
        try {
            allDivisionNames.clear();
            statement = JDBCConnectionHelper.getStatement();
            String query = "SELECT Division FROM client_schedule.first_level_divisions;";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                divisionName = resultSet.getString("Division");
                allDivisionNames.add(divisionName);
            }
            return allDivisionNames;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
