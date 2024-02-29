package net.jacktools.barcode.barcodegenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.jacktools.barcode.barcodegenerator.utils.AppLog;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.awt.*;
import java.util.logging.Level;

public class Application extends javafx.application.Application {

    public static TrayIcon TRAY_ICON;
    private static SystemTray TRAY;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (SystemTray.isSupported()) {
            TRAY = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/img/logo.jpg"));
            TRAY_ICON = new TrayIcon(image, Assets.getString("application.title"));
            //Let the system resize the image if needed
            TRAY_ICON.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            TRAY_ICON.setToolTip(Assets.getString("application.tray.tooltip"));
            TRAY.add(TRAY_ICON);
        }
        AppLog.log(Level.INFO, Assets.getString("application.log.start"));
        if (Settings.WEB_SERVER_AUTOSTART) {
            AppServer.start();
            AppServer.LOG(Assets.getString("application.tray.webserver.start", Settings.WEB_SERVER_PORT));
            if (null != Application.TRAY_ICON) {
                Application.TRAY_ICON.displayMessage(Assets.getString("application.title"), Assets.getString("application.tray.webserver.start", Settings.WEB_SERVER_PORT), TrayIcon.MessageType.INFO);
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
        fxmlLoader.setResources(Assets.RESOURCEBUNDLE);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle(Assets.getString("application.title"));
        stage.getIcons().add(Assets.APPLICATION_ICON);
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(750);
        MainViewController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setDefaultValues();
        controller.setDefaultEpcValues();
        controller.setDefaultWiFiValues();
        stage.show();
    }
}