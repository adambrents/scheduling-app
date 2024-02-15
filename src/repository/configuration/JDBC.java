package repository.configuration;

import configuration.ConnectionAuth;
import configuration.ConnectionConfig;
import constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static constants.Constants.CONNECTION_CONFIG;

public abstract class JDBC {
    private static final ConnectionAuth connectionAuth = CONNECTION_CONFIG.getConnectionAuth();
    private static final String JDBC_URL = String.format("jdbc:mysql://localhost/%s?connectionTimeZone=SERVER", connectionAuth.getDb());
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, connectionAuth.getUserName(), connectionAuth.getPassword());
    }

    private static String getPasswordFromVault() {
        // Implement secure retrieval of password from vault or environment variables
        return "Passw0rd!";
    }
}
