package net.jacktools.barcode.barcodegenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("i18n", Locale.getDefault()));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Barcode generator");
        stage.setScene(scene);
        stage.show();
    }
}