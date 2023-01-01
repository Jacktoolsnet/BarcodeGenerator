package net.jacktools.barcode.barcodegenerator.utils;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Assets {

    public static ResourceBundle RESOURCEBUNDLE;
    public static Image APPLICATION_ICON;

    static {
        RESOURCEBUNDLE = ResourceBundle.getBundle("i18n", Locale.getDefault());
        APPLICATION_ICON = new Image(Assets.class.getResourceAsStream("/img/logo.jpg"));
    }

    public static String getString(String key) {
        try {
            return RESOURCEBUNDLE.getString(key);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getString(String key, Object... args) {
        return MessageFormat.format(getString(key), args);
    }

    /**
     * Create and returns an alert.
     *
     * @param alertType        the alert type
     * @param alertAlwaysOnTop should the alert stay always on top?
     * @return the created alert
     */
    public static Alert getAlert(Alert.AlertType alertType, boolean alertAlwaysOnTop) {
        Alert alert = new Alert(alertType);
        alert.setTitle(getString("application.title"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(APPLICATION_ICON);
        stage.setAlwaysOnTop(alertAlwaysOnTop);
        return alert;
    }

}
