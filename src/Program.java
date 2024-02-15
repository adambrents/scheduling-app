import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.Integer.parseInt;

public class Program extends Application{
    /**
     * Loads login screen on app startup
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * opens the a connection to the db
     * closes connection after user exits out of args     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}