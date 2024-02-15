package configuration;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class ConnectionConfig {
    private ConnectionAuth connectionAuth;

    public ConnectionAuth getConnectionAuth() {
        if (connectionAuth == null) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader("config.json")) {
                ConnectionConfig config = gson.fromJson(reader, ConnectionConfig.class);
                setConnectionAuth(config.connectionAuth);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connectionAuth;
    }
    private void setConnectionAuth(ConnectionAuth connectionAuth) {
        this.connectionAuth = connectionAuth;
    }
}
