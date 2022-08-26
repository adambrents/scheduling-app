package utilities;

import database.Customers;
import database.JDBC;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnectionHelper {
    /**
     * gets statement to run queries in db
     * @return
     */
    public static Statement getStatement(){
        try {
            Statement statement = JDBC.getConnection().createStatement();
            return statement;

        }catch (SQLException e){
            return null;
        }
    }
}
