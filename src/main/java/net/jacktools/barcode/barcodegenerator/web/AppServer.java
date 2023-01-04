package net.jacktools.barcode.barcodegenerator.web;

import com.sun.net.httpserver.HttpServer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.jacktools.barcode.barcodegenerator.utils.AppLog;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.web.routes.*;

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
        HTTP_SERVER.createContext("/aztec", new AztecHandler());
        HTTP_SERVER.createContext("/code39", new Code39Handler());
        HTTP_SERVER.createContext("/code93", new Code93Handler());
        HTTP_SERVER.createContext("/code128", new Code128Handler());
        HTTP_SERVER.createContext("/codebar", new CodeBarHandler());
        HTTP_SERVER.createContext("/datamatrix", new DataMatrixHandler());
        HTTP_SERVER.createContext("/ean8", new Ean8Handler());
        HTTP_SERVER.createContext("/ean13", new Ean13Handler());
        HTTP_SERVER.createContext("/itf", new ItfHandler());
        HTTP_SERVER.createContext("/pdf417", new Pdf417Handler());
        HTTP_SERVER.createContext("/qrcode", new QrCodeHandler());
        HTTP_SERVER.createContext("/upca", new UpcaHandler());
        HTTP_SERVER.createContext("/upce", new UpceHandler());
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
