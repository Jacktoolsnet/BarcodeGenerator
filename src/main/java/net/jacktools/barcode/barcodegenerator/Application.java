package net.jacktools.barcode.barcodegenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.jacktools.barcode.barcodegenerator.utils.Assets;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private static Assets ASSETS = new Assets();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
        fxmlLoader.setResources(Assets.RESOURCEBUNDLE);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Barcode generator");
        stage.setScene(scene);
        stage.show();
    }
}