package database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utilities.JDBCConnectionHelper;

import java.sql.ResultSet;
import java.sql.Statement;

public class Users {

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    private static Statement statement;

    /**
     * Validates user credentials with the db and returns a numeric value for what credential element is incorrect/correct
     *
     * @param user
     * @return
     */

    public static int submit(User user){
        int valid = 0;
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                User allUser = new User(
                    resultSet.getString("User_Name"),
                    resultSet.getInt("User_ID"),
                    resultSet.getString("Password")
                );
                allUsers.add(allUser);
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        int i = 0;
        while (i < allUsers.size()){
            boolean bothAreWrong = !user.getUsername().equals(allUsers.get(i).getUsername()) && !user.getPassword().equals(allUsers.get(i).getPassword());
            if(!user.getPassword().equals(allUsers.get(i).getPassword())){
                valid = 1;
                if (bothAreWrong){
                    valid = 3;

                }
            }
            else if(!user.getUsername().equals(allUsers.get(i).getUsername())){
                valid = 2;
                if (bothAreWrong){
                    valid = 3;
                }
            }
            else{
                valid = 0;
                break;
            }
            i++;
        }
        return valid;
    }

    /**
     * returns an array of all users
     * @return
     */
    public static ObservableList<User> getAllUsers(){
        allUsers.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users;";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                User allUser = new User(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
                allUsers.add(allUser);
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return allUsers;
    }

    /**
     * returns a user object in response to a username
     *
     * @param userName
     * @return
     */
    public static User getUserByName(String userName){
        allUsers.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name='" + userName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                User allUser = new User(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
                return allUser;
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * returns a user object in response to a userid
     * @param userId
     * @return
     */
    public static User getUserById(int userId){
        allUsers.clear();
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users WHERE User_ID=" + userId + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                User allUser = new User(
                        resultSet.getString("User_Name"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Password")
                );
                return allUser;
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
