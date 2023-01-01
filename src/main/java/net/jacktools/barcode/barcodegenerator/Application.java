package net.jacktools.barcode.barcodegenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.jacktools.barcode.barcodegenerator.utils.AppLog;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.io.IOException;
import java.util.logging.Level;

public class Application extends javafx.application.Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppLog.log(Level.INFO, Assets.getString("application.log.start"));
        if (Settings.WEB_SERVER_AUTOSTART) {
            AppServer.start();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
        fxmlLoader.setResources(Assets.RESOURCEBUNDLE);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(Assets.getString("application.title"));
        stage.getIcons().add(Assets.APPLICATION_ICON);
        stage.setScene(scene);
        MainViewController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.show();
    }
}