package net.jacktools.barcode.barcodegenerator.web;

import com.sun.net.httpserver.HttpServer;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.web.routes.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class AppServer {

    private static HttpServer HTTP_SERVER;

    /**
     * Stats the http web server.
     *
     * @throws IOException
     */
    public static void start() throws IOException {
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
    }

    /**
     * Stops the http web server.
     */
    public static void stop() {
        if (null != HTTP_SERVER) {
            HTTP_SERVER.stop(0);
        }
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
}
