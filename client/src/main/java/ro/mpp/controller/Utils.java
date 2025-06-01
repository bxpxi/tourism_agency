package ro.mpp.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import ro.mpp.Application;
import ro.mpp.IAppService;

import java.io.IOException;

public class Utils {
    public static Stage createWindow(String fxmlFile, int width, int height) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = getLoader(fxmlFile);
            Scene scene = new Scene(loader.load(), width, height);
            stage.setUserData(loader);
            stage.setResizable(false);
            stage.setScene(scene);
            return stage;
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage createWindow(String fxmlFile, int width, int height, String title, IAppService appService) {
        Stage stage = createWindow(fxmlFile, width, height);
        Controller controller = getController(stage);
        controller.setAppService(appService);
        stage.setTitle(title);
        return stage;
    }

    public static Stage createLoginWindow(IAppService appService) {
        return createWindow("/loginView.fxml", 488, 352, "Employee Login", appService);
    }

    public static Stage createMainWindow(IAppService appService) {
        return createWindow("/mainView.fxml", 824, 600, "Flights", appService);
    }

    public static void showErrorBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert Box");
        alert.setContentText(message);
        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK) {
                System.out.println("OK");
            }
        });
    }

    public static <C extends Controller> C getController(Stage stage){
        return ((FXMLLoader)stage.getUserData()).getController();
    }
    public static FXMLLoader getLoader(String name) {
        return new FXMLLoader(Application.class.getResource(name));
    }
}
