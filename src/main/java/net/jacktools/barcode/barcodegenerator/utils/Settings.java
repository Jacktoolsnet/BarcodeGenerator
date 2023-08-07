package net.jacktools.barcode.barcodegenerator.utils;

import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.epc.SupportedCurrency;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public class Settings {

    public static String APP_ICON_COLOR;
    public static String APP_INVALID_VALUE_COLOR;
    public static int WEB_SERVER_PORT;
    public static int WEB_SERVER_PORT_MIN;
    public static int WEB_SERVER_PORT_MAX;
    public static boolean WEB_SERVER_AUTOSTART;
    public static int BARCODE_DEFAULT_MARGIN;
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
    // Epc Code
    public static String BIC;
    public static String PAYEE;
    public static String IBAN;
    public static SupportedCurrency CURRENCY;
    public static Double PAYMENT_AMOUNT;
    public static String PURPOSE;
    public static String REFERENCE;
    public static String PURPOSE_OF_USE;
    public static String NOTICE;
    private static final Path configPropertiesPath;
    private static final Properties properties = new Properties();

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
        APP_ICON_COLOR = properties.getProperty("app.icon.color");
        APP_INVALID_VALUE_COLOR = properties.getProperty("app.invalid.value.color");
        WEB_SERVER_PORT = Integer.valueOf(properties.getProperty("webserver.port"));
        WEB_SERVER_PORT_MIN = Integer.valueOf(properties.getProperty("webserver.port.min"));
        WEB_SERVER_PORT_MAX = Integer.valueOf(properties.getProperty("webserver.port.max"));
        WEB_SERVER_AUTOSTART = Boolean.valueOf(properties.getProperty("webserver.autostart"));
        BARCODE_DEFAULT_MARGIN = Integer.valueOf(properties.getProperty("barcode.default.margin"));
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
        // Epc Code
        BIC = !"null".equals(properties.getProperty("epc.bic")) ? properties.getProperty("epc.bic") : "";
        PAYEE = !"null".equals(properties.getProperty("epc.payee")) ? properties.getProperty("epc.payee") : "";
        IBAN = !"null".equals(properties.getProperty("epc.iban")) ? properties.getProperty("epc.iban") : "";
        CURRENCY = !"null".equals(properties.getProperty("epc.currency")) ? SupportedCurrency.valueOf(properties.getProperty("epc.currency")) : SupportedCurrency.EUR;
        PAYMENT_AMOUNT = !"null".equals(properties.getProperty("epc.paymentamount")) ? Double.valueOf(properties.getProperty("epc.paymentamount")) : 0.00;
        PURPOSE = !"null".equals(properties.getProperty("epc.purpose")) ? properties.getProperty("epc.purpose") : "";
        REFERENCE = !"null".equals(properties.getProperty("epc.reference")) ? properties.getProperty("epc.reference") : "";
        PURPOSE_OF_USE = !"null".equals(properties.getProperty("epc.purposeofuse")) ? properties.getProperty("epc.purposeofuse") : "";
        NOTICE = !"null".equals(properties.getProperty("epc.notice")) ? properties.getProperty("epc.notice") : "";
    }

    public static void saveProperties() {
        AppLog.log(Level.INFO, Assets.getString("application.settings.save"));
        try (OutputStream output = new FileOutputStream(configPropertiesPath.toString())) {
            properties.setProperty("app.icon.color", String.valueOf(APP_ICON_COLOR));
            properties.setProperty("app.invalid.value.color", String.valueOf(APP_INVALID_VALUE_COLOR));
            // Webserver
            properties.setProperty("webserver.port", String.valueOf(WEB_SERVER_PORT));
            properties.setProperty("webserver.port.min", String.valueOf(WEB_SERVER_PORT_MIN));
            properties.setProperty("webserver.port.max", String.valueOf(WEB_SERVER_PORT_MAX));
            properties.setProperty("webserver.autostart", String.valueOf(WEB_SERVER_AUTOSTART));
            // Barcode
            properties.setProperty("barcode.default.margin", String.valueOf(BARCODE_DEFAULT_MARGIN));
            properties.setProperty("barcode.default.width", String.valueOf(BARCODE_DEFAULT_WIDTH));
            properties.setProperty("barcode.min.width", String.valueOf(BARCODE_MIN_WIDTH));
            properties.setProperty("barcode.max.width", String.valueOf(BARCODE_MAX_WIDTH));
            properties.setProperty("barcode.default.height", String.valueOf(BARCODE_DEFAULT_HEIGHT));
            properties.setProperty("barcode.min.height", String.valueOf(BARCODE_MIN_HEIGHT));
            properties.setProperty("barcode.color", String.valueOf(BARCODE_COLOR));
            properties.setProperty("barcode.color.background", String.valueOf(BARCODE_BACKGROUND_COLOR));
            properties.setProperty("barcode.value", String.valueOf(BARCODE_VALUE));
            properties.setProperty("barcode.type", String.valueOf(BARCODE_TYPE));
            // QR-Code
            properties.setProperty("qrcode.default.width", String.valueOf(QRCODE_DEFAULT_WIDTH));
            properties.setProperty("qrcode.min.width", String.valueOf(QRCODE_MIN_WIDTH));
            properties.setProperty("qrcode.max.width", String.valueOf(QRCODE_MAX_WIDTH));
            properties.setProperty("qrcode.default.height", String.valueOf(QRCODE_DEFAULT_HEIGHT));
            properties.setProperty("qrcode.min.height", String.valueOf(QRCODE_MIN_HEIGHT));
            properties.setProperty("qrcode.color", String.valueOf(QRCODE_COLOR));
            properties.setProperty("qrcode.color.background", String.valueOf(QRCODE_BACKGROUND_COLOR));
            properties.setProperty("qrcode.value", String.valueOf(QRCODE_VALUE));
            // Epc Code
            properties.setProperty("epc.bic", String.valueOf(BIC));
            properties.setProperty("epc.payee", String.valueOf(PAYEE));
            properties.setProperty("epc.iban", String.valueOf(IBAN));
            properties.setProperty("epc.currency", String.valueOf(CURRENCY));
            properties.setProperty("epc.paymentamount", String.valueOf(PAYMENT_AMOUNT));
            properties.setProperty("epc.purpose", String.valueOf(PURPOSE));
            properties.setProperty("epc.reference", String.valueOf(REFERENCE));
            properties.setProperty("epc.purposeofuse", String.valueOf(PURPOSE_OF_USE));
            properties.setProperty("epc.notice", String.valueOf(NOTICE));
            // Save
            properties.store(output, null);
        } catch (IOException io) {
            AppLog.log(Level.SEVERE, Assets.getString("application.settings.save.error"));
            AppLog.log(Level.SEVERE, io.toString());
        }
    }

}
