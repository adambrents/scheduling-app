package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {
    @FXML
    private Button exitBtn;
    @FXML
    private Label loginScreenLbl;
    @FXML
    private Label password;
    @FXML
    private Label passwordError;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button submitBtn;
    @FXML
    private Label username;
    @FXML
    private Label usernameError;
    @FXML
    private TextField usernameField;
    @FXML
    private Label zoneIDLbl;
    private Parent scene;
    @FXML
    void onActionExit(ActionEvent event) {

    }
    @FXML
    void onActionSubmit(ActionEvent event) throws IOException {
        Locale locale = Locale.getDefault();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Utilities/ResourceBundle", locale);

        FileWriter loginAttempts = new FileWriter("login_attempts.txt", true);
        PrintWriter loginAttempt = new PrintWriter(loginAttempts);

        String username = usernameField.getId();
        boolean validUserName = true;
    }

}
