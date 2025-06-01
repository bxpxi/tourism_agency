package ro.mpp;

import javafx.stage.Stage;
import ro.mpp.client.ServiceObjectProxy;
import ro.mpp.controller.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        var props = loadProps();
        var ip = props.getProperty("ip");
        var port = Integer.parseInt(props.getProperty("port"));
        var server = new ServiceObjectProxy(ip, port);
        Utils.createLoginWindow(server).show();
    }

    public static Properties loadProps() {
        var props = new Properties();
        try {
            props.load(new FileReader("bd.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.properties" + e);
        }

        return props;
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
