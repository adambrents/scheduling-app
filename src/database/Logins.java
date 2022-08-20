package database;

import model.User;
import utilities.JDBCConnectionHelper;

import java.sql.ResultSet;
import java.sql.Statement;

public class Logins {
    private static User validUser;
    private static Statement statement;

    public static Boolean submit(String UserName, String Password){
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name='" + UserName + "' AND Password='" + Password + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                validUser = new User();
                validUser.setUserName(resultSet.getString("User_Name"));
                statement.close();
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public static Boolean submit(String UserName){
        try {
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name='" + UserName + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                validUser = new User();
                validUser.setUserName(resultSet.getString("User_Name"));
                statement.close();
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public static int getUserId(String userName){
        try{
            statement = JDBCConnectionHelper.getStatement();
            String sql = "SELECT User_ID FROM client_schedule.users WHERE User_Name ='" + userName + "';";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int userId = resultSet.getInt(1);//1 is the User_ID column in client_schedule.users
                return userId;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
