package controller;

import database.Appointments;
import database.Logins;
import database.Times;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public boolean appointmentsWithin15Min;

    private Parent scene;

    /**
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
                    int index = 0;
                    while (index < Appointments.getWeeklyAppointments().size()){
                        Timestamp startTimestamp = Appointments.getWeeklyAppointments().get(index).getStart();
                        LocalDateTime startDateTime = startTimestamp.toLocalDateTime();
                        if(startDateTime.isAfter(LocalDateTime.now()) && startDateTime.isBefore(LocalDateTime.now().plusMinutes(15))){
                            appointmentsWithin15Min = true;
                            break;
                        }
                        else {
                            index++;
                        }
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    if(appointmentsWithin15Min){
                        alert.setContentText("You have an Appointment within 15 minutes");
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
                System.out.println(e.getMessage());
                //Lambda expression to handle user response. Reloads login page on OK
                alert.showAndWait().ifPresent((response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Alerting!");
                        Parent main = null;
                        try {
                            main = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
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
     * @param actionEvent
     */
    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
