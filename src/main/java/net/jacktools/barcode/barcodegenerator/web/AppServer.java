package net.jacktools.barcode.barcodegenerator.web;

import com.sun.net.httpserver.HttpServer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.jacktools.barcode.barcodegenerator.utils.AppLog;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.routes.*;
import net.jacktools.barcode.barcodegenerator.web.routes.epc.EpcCodeHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AppServer {

    private static HttpServer HTTP_SERVER;

    public static BooleanProperty RUNNING = new SimpleBooleanProperty();
    public static BooleanProperty NOT_RUNNING = new SimpleBooleanProperty();

    public static StringProperty LOG_PROPERTY = new SimpleStringProperty();

    /**
     * Stats the http web server.
     *
     * @throws IOException
     */
    public static void start() throws IOException {
        LOG_PROPERTY.set("");
        LOG(Assets.getString("application.log.start"));
        RUNNING.set(false);
        NOT_RUNNING.set(true);
        HTTP_SERVER = HttpServer.create(new InetSocketAddress(Settings.WEB_SERVER_PORT), 0);
        HTTP_SERVER.createContext(SupportedBarcodeFormat.AZTEC.getRoute(), new AztecHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.CODE_39.getRoute(), new Code39Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.CODE_93.getRoute(), new Code93Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.CODE_128.getRoute(), new Code128Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.CODABAR.getRoute(), new CodeBarHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.DATA_MATRIX.getRoute(), new DataMatrixHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.EAN_8.getRoute(), new Ean8Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.EAN_13.getRoute(), new Ean13Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.ITF.getRoute(), new ItfHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.PDF_417.getRoute(), new Pdf417Handler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.QR_CODE.getRoute(), new QrCodeHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.UPC_A.getRoute(), new UpcaHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.UPC_E.getRoute(), new UpceHandler());
        HTTP_SERVER.createContext(SupportedBarcodeFormat.EPC_CODE.getRoute(), new EpcCodeHandler());
        HTTP_SERVER.setExecutor(null); // creates a default executor
        HTTP_SERVER.start();
        RUNNING.set(true);
        NOT_RUNNING.set(false);
    }

    /**
     * Stops the http web server.
     */
    public static void stop() {
        if (null != HTTP_SERVER) {
            HTTP_SERVER.stop(0);
        }
        RUNNING.set(false);
        NOT_RUNNING.set(true);
    }

    public static Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    /**
     * Add a Message to the AppServer log.
     *
     * @param message
     */
    public static void LOG(String message) {
        AppLog.log(Level.INFO, message);
        SimpleDateFormat logTime = new SimpleDateFormat("yyy.MM.dd HH:mm:ss.SSS");
        Calendar cal = new GregorianCalendar();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logTime.format(cal.getTime()))
                .append(" || ")
                .append(message + "\n");
        LOG_PROPERTY.set(null != LOG_PROPERTY.get() ? stringBuilder + LOG_PROPERTY.get() : stringBuilder.toString());
    }
}
