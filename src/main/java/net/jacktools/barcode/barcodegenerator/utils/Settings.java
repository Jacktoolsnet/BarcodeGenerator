package net.jacktools.barcode.barcodegenerator.utils;

import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public class Settings {

    public static String APP_ICON_COLOUR;
    public static int WEB_SERVER_PORT;
    public static boolean WEB_SERVER_AUTOSTART;
    public static int BARCODE_DEFAULT_HEIGHT;
    public static int BARCODE_DEFAULT_WIDTH;
    public static int BARCODE_MAX_HEIGHT;
    public static int BARCODE_MAX_WIDTH;
    public static int BARCODE_MIN_HEIGHT;
    public static int BARCODE_MIN_WIDTH;
    public static Color BARCODE_COLOR;
    public static Color BARCODE_BACKGROUND_COLOR;
    public static String BARCODE_VALUE;
    public static SupportedBarcodeFormat BARCODE_TYPE;
    public static int QRCODE_DEFAULT_HEIGHT;
    public static int QRCODE_DEFAULT_WIDTH;
    public static int QRCODE_MAX_HEIGHT;
    public static int QRCODE_MAX_WIDTH;
    public static int QRCODE_MIN_HEIGHT;
    public static int QRCODE_MIN_WIDTH;
    public static Color QRCODE_COLOR;
    public static Color QRCODE_BACKGROUND_COLOR;
    public static String QRCODE_VALUE;
    private static Path configPropertiesPath;
    private static Properties properties = new Properties();

    static {
        configPropertiesPath = Paths.get(System.getProperty("user.home")).resolve(Assets.getString("application.path.root")).resolve(Assets.getString("application.path.env")).resolve("config.properties");
        try {
            Files.createDirectories(configPropertiesPath.getParent());
        } catch (IOException e) {
            AppLog.log(Level.SEVERE, e.toString());
        }
        loadAppProperties();
    }

    private static void loadAppProperties() {
        AppLog.log(Level.INFO, Assets.getString("application.settings.read.app"));
        try (InputStream input = new FileInputStream(configPropertiesPath.toString())) {
            properties.load(input);
            readProperties();
        } catch (Exception ex) {
            AppLog.log(Level.INFO, Assets.getString("application.settings.read.error"));
            loadDefaultProperties();
        }
    }

    private static void loadDefaultProperties() {
        try (InputStream input = Settings.class.getResourceAsStream("/env/config.properties")) {
            AppLog.log(Level.INFO, Assets.getString("application.settings.read.default"));
            properties.load(input);
            readProperties();
        } catch (Exception e) {
            AppLog.log(Level.SEVERE, Assets.getString("application.settings.read.error"));
            AppLog.log(Level.SEVERE, e.toString());
        }
    }

    private static void readProperties() throws NumberFormatException {
        APP_ICON_COLOUR = properties.getProperty("app.icon.colour");
        WEB_SERVER_PORT = Integer.valueOf(properties.getProperty("webserver.port"));
        WEB_SERVER_AUTOSTART = Boolean.valueOf(properties.getProperty("webserver.autostart"));
        BARCODE_DEFAULT_WIDTH = Integer.valueOf(properties.getProperty("barcode.default.width"));
        BARCODE_MIN_WIDTH = Integer.valueOf(properties.getProperty("barcode.min.width"));
        BARCODE_MAX_WIDTH = Integer.valueOf(properties.getProperty("barcode.max.width"));
        BARCODE_DEFAULT_HEIGHT = Integer.valueOf(properties.getProperty("barcode.default.height"));
        BARCODE_MIN_HEIGHT = Integer.valueOf(properties.getProperty("barcode.min.height"));
        BARCODE_MAX_HEIGHT = Integer.valueOf(properties.getProperty("barcode.max.height"));
        BARCODE_COLOR = Color.valueOf(properties.getProperty("barcode.color"));
        BARCODE_BACKGROUND_COLOR = Color.valueOf(properties.getProperty("barcode.color.background"));
        BARCODE_VALUE = properties.getProperty("barcode.value");
        BARCODE_TYPE = SupportedBarcodeFormat.valueOf(properties.getProperty("barcode.type"));
        QRCODE_DEFAULT_WIDTH = Integer.valueOf(properties.getProperty("qrcode.default.width"));
        QRCODE_MIN_WIDTH = Integer.valueOf(properties.getProperty("qrcode.min.width"));
        QRCODE_MAX_WIDTH = Integer.valueOf(properties.getProperty("qrcode.max.width"));
        QRCODE_DEFAULT_HEIGHT = Integer.valueOf(properties.getProperty("qrcode.default.height"));
        QRCODE_MIN_HEIGHT = Integer.valueOf(properties.getProperty("qrcode.min.height"));
        QRCODE_MAX_HEIGHT = Integer.valueOf(properties.getProperty("qrcode.max.height"));
        QRCODE_COLOR = Color.valueOf(properties.getProperty("qrcode.color"));
        QRCODE_VALUE = properties.getProperty("qrcode.value");
        QRCODE_BACKGROUND_COLOR = Color.valueOf(properties.getProperty("qrcode.color.background"));
    }

    public static void saveProperties() {
        AppLog.log(Level.INFO, Assets.getString("application.settings.save"));
        try (OutputStream output = new FileOutputStream(configPropertiesPath.toString())) {
            properties.setProperty("app.icon.colour", APP_ICON_COLOUR);
            properties.setProperty("webserver.port", String.valueOf(WEB_SERVER_PORT));
            properties.setProperty("webserver.autostart", String.valueOf(WEB_SERVER_AUTOSTART));
            // Barcode
            properties.setProperty("barcode.default.width", String.valueOf(BARCODE_DEFAULT_WIDTH));
            properties.setProperty("barcode.min.width", String.valueOf(BARCODE_MIN_WIDTH));
            properties.setProperty("barcode.max.width", String.valueOf(BARCODE_MAX_WIDTH));
            properties.setProperty("barcode.default.height", String.valueOf(BARCODE_DEFAULT_HEIGHT));
            properties.setProperty("barcode.min.height", String.valueOf(BARCODE_MIN_HEIGHT));
            properties.setProperty("barcode.color", BARCODE_COLOR.toString());
            properties.setProperty("barcode.color.background", BARCODE_BACKGROUND_COLOR.toString());
            properties.setProperty("barcode.value", BARCODE_VALUE);
            properties.setProperty("barcode.type", BARCODE_TYPE.toString());
            // QR-Code
            properties.setProperty("qrcode.default.width", String.valueOf(QRCODE_DEFAULT_WIDTH));
            properties.setProperty("qrcode.min.width", String.valueOf(QRCODE_MIN_WIDTH));
            properties.setProperty("qrcode.max.width", String.valueOf(QRCODE_MAX_WIDTH));
            properties.setProperty("qrcode.default.height", String.valueOf(QRCODE_DEFAULT_HEIGHT));
            properties.setProperty("qrcode.min.height", String.valueOf(QRCODE_MIN_HEIGHT));
            properties.setProperty("qrcode.color", QRCODE_COLOR.toString());
            properties.setProperty("qrcode.color.background", QRCODE_BACKGROUND_COLOR.toString());
            properties.setProperty("qrcode.value", QRCODE_VALUE);
            // Save
            properties.store(output, null);
        } catch (IOException io) {
            AppLog.log(Level.SEVERE, Assets.getString("application.settings.save.error"));
            AppLog.log(Level.SEVERE, io.toString());
        }
    }

}
