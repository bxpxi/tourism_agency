package ro.mpp.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp.Employee;
import ro.mpp.ValidationException;

public class LoginViewController extends Controller {
    @FXML
    TextField agencyNameField;

    @FXML
    TextField passwordField;

    @FXML
    Button loginButton;

    public void onLoginButtonClicked() {
        String agencyName = agencyNameField.getText();
        String password = passwordField.getText();
        Employee employee = null;
        Stage window = null;

        try {
            window = Utils.createMainWindow(appService);
            MainViewController controller = Utils.getController(window);
            employee = appService.login(agencyName, password, controller);
        } catch(ValidationException e) {

        }

        if(employee == null) {
            showException(new ValidationException("Login failed. Incorrect password or agency name"));
            Platform.exit();
        } else {
            Employee emp = employee;
            window.setOnCloseRequest(event -> {
                appService.logout(emp);
                Platform.exit();
                System.exit(0);
            });

            window.show();
            loginButton.getScene().getWindow().hide();
        }


        /*
        try {
            Employee employee = appService.login(agencyName, password);
            if(employee == null) {
                showException(new ValidationException("Invalid username or password"));
            }
            Utils.createMainWindow(appService).show();
        } catch (Exception e) {
            showException(e);
        }
        */
    }
}