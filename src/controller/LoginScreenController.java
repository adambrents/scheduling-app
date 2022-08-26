package controller;

import database.Appointments;
import database.Logins;
import database.Times;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import utilities.TimeHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    public Label zoneID;
    public Label loginForm;
    public Button submit;
    public Label username;
    public Label password;
    public Label usernameError;
    public Label passwordError;
    public TextField usernameField;
    public TextField passwordField;
    public Button exit;
    private Parent scene;

    /**
     * Validates if user exists and if information was entered correctly. Loads main screen
     * Displays a notification of any upcoming appointments within 15 minutes
     * writes a log to the login attempts log
     * @param actionEvent
     * @throws IOException
     */
    public void onSubmit(ActionEvent actionEvent) throws IOException {
        //gets resource bundles for use further down
        Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Utilities/ResourceBundle", locale);

        //records login activity/attempts
        FileWriter loginAttempts = new FileWriter("login_acitivity.txt", true);
        PrintWriter loginAttempt = new PrintWriter(loginAttempts);


        String usrnm = usernameField.getText();
        boolean validUsername = Logins.submit(usrnm);
        if (validUsername == true) {
            String pssword = passwordField.getText();
            boolean isUser = Logins.submit(usrnm, pssword);
            try{
                if (isUser != true) {
                    passwordError.setText(resourceBundle.getString("PasswordError"));
                    usernameError.setText("");
                    loginAttempt.println(Times.getTimeStamp() + " Unsuccessful Login UserName: " + usernameField.getText() + " Invalid password");
                    loginAttempt.close();
                }
                else {
                    loginAttempt.println(Times.getTimeStamp() + " Successful Login UserName: " + usernameField.getText());
                    loginAttempt.close();
                    Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainScreen.fxml"));
                    scene = loader.load();
                    MainScreenController controller = loader.getController();
                    controller.setUser(Logins.getUserId(usernameField.getText()));
                    stage.setScene(new Scene(scene));
                    stage.show();

                    Appointment appt15Min = Appointments.get15MinAppt();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if(appt15Min != null){
                        alert.setContentText("You have the following appointment witin the next 15 minutes: \n\nAppointment ID: " + appt15Min.getAppointmentID()
                                + "\nDate: " + appt15Min.getStartDate() + "\nTime: " + appt15Min.getStartTime());
                    }
                    else {
                        alert.setContentText("You have no Appointments within 15 minutes");
                    }
                    alert.show();
                }
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error: See console for more details");
                e.printStackTrace();
                //Lambda expression to handle user response. Reloads login page on OK
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Alerting!");
                        Parent main = null;
                        try {
                            main = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
                            Scene scene = new Scene(main);
                            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }));
                return;
            }

        } else {
            loginAttempt.println(Times.getTimeStamp() + " Unsuccessful Login UserName: " + usernameField.getText());
            loginAttempt.close();
            usernameError.setText(resourceBundle.getString("UsernameError"));
            passwordError.setText("");
        }
    }

    /**
     * Sets fields to user's system specific details
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("Utilities/ResourceBundle", locale);
        username.setText(resourceBundle.getString("Username"));
        loginForm.setText(resourceBundle.getString("Login"));
        password.setText(resourceBundle.getString("Password"));
        submit.setText(resourceBundle.getString("Submit"));


        ZoneId zone = ZoneId.systemDefault();
        zoneID.setText(zone.toString());
    }

    /**
     * exits the application
     * @param actionEvent
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
