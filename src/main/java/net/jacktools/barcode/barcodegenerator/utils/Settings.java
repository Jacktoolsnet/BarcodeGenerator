package net.jacktools.barcode.barcodegenerator.utils;

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
    public static int BARCODE_DEFAULT_WIDTH;
    public static int BARCODE_DEFAULT_HEIGHT;
    public static int QRCODE_DEFAULT_WIDTH;
    public static int QRCODE_DEFAULT_HEIGHT;
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
        BARCODE_DEFAULT_HEIGHT = Integer.valueOf(properties.getProperty("barcode.default.height"));
        QRCODE_DEFAULT_WIDTH = Integer.valueOf(properties.getProperty("qrcode.default.width"));
        QRCODE_DEFAULT_HEIGHT = Integer.valueOf(properties.getProperty("qrcode.default.height"));
    }

    public static void saveProperties() {
        AppLog.log(Level.INFO, Assets.getString("application.settings.save"));
        try (OutputStream output = new FileOutputStream(configPropertiesPath.toString())) {
            properties.setProperty("app.icon.colour", APP_ICON_COLOUR);
            properties.setProperty("webserver.port", String.valueOf(WEB_SERVER_PORT));
            properties.setProperty("webserver.autostart", String.valueOf(WEB_SERVER_AUTOSTART));
            properties.setProperty("barcode.default.width", String.valueOf(BARCODE_DEFAULT_WIDTH));
            properties.setProperty("barcode.default.height", String.valueOf(BARCODE_DEFAULT_HEIGHT));
            properties.setProperty("qrcode.default.width", String.valueOf(QRCODE_DEFAULT_WIDTH));
            properties.setProperty("qrcode.default.height", String.valueOf(QRCODE_DEFAULT_HEIGHT));
            properties.store(output, null);
        } catch (IOException io) {
            AppLog.log(Level.SEVERE, Assets.getString("application.settings.save.error"));
            AppLog.log(Level.SEVERE, io.toString());
        }
    }

}
