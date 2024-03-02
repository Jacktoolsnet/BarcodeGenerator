package net.jacktools.barcode.barcodegenerator.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Assets {

    public static ResourceBundle RESOURCEBUNDLE;
    public static Image APPLICATION_ICON;

    static {
        RESOURCEBUNDLE = ResourceBundle.getBundle("i18n", Locale.getDefault());
        APPLICATION_ICON = new Image(Objects.requireNonNull(Assets.class.getResourceAsStream("/img/logo.jpg")));
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
    public static Alert getAlert(Alert.AlertType alertType, boolean alertAlwaysOnTop, Stage parentStage) {
        Alert alert = new Alert(alertType);
        alert.setTitle(getString("application.title"));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(APPLICATION_ICON);
        stage.setAlwaysOnTop(alertAlwaysOnTop);
        if (null != parentStage) {
            Screen screen = getScreenFromWindow(parentStage.getScene().getWindow());
            Rectangle2D screenBounds = screen.getVisualBounds();
            if (alert.getDialogPane().getScene().getWidth() > screenBounds.getWidth()) {
                alert.setX(screen.getBounds().getMinX());
            } else {
                alert.setX(screenBounds.getMinX() + screenBounds.getWidth() / 2d - alert.getDialogPane().getWidth() / 2d);
            }
            // At this point the height is zero. I don't know why.
            alert.setY(screenBounds.getMinY() + screenBounds.getHeight() / 2d - screenBounds.getHeight() * 0.15d);
        }
        return alert;
    }

    /**
     * Returns the screen for the given Window
     *
     * @param window the window
     * @return the sreen
     */
    private static Screen getScreenFromWindow(Window window) {
        for (Screen screen : Screen.getScreensForRectangle(window.getX() + 10, window.getY() + 10, window.getWidth() - 20, window.getHeight() - 20)) {
            return screen;
        }
        return Screen.getPrimary();
    }

}
