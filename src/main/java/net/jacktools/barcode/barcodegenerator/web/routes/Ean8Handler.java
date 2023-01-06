package net.jacktools.barcode.barcodegenerator.web.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Ean8Handler implements HttpHandler {
    // Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150))
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            AppServer.LOG(httpExchange.getRequestURI().toString());
            Map<String, String> queryParameter = null != httpExchange.getRequestURI().getQuery() ? AppServer.queryToMap(httpExchange.getRequestURI().getQuery()) : new HashMap<>();
            byte[] bytes = Barcode.createByteArray(null != queryParameter.get("value") ? URLDecoder.decode(queryParameter.get("value")) : Settings.BARCODE_VALUE, SupportedBarcodeFormat.EAN_8, null != queryParameter.get("width") ? Integer.valueOf(queryParameter.get("width")) : Settings.BARCODE_DEFAULT_WIDTH, null != queryParameter.get("height") ? Integer.valueOf(queryParameter.get("height")) : Settings.BARCODE_DEFAULT_HEIGHT, null != queryParameter.get("backgroundcolor") ? Color.valueOf(queryParameter.get("backgroundcolor")) : Settings.BARCODE_BACKGROUND_COLOR, null != queryParameter.get("color") ? Color.valueOf(queryParameter.get("color")) : Settings.BARCODE_COLOR);
            httpExchange.sendResponseHeaders(200, bytes.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(bytes);
            outputStream.close();
            AppServer.LOG("OK");
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, e.toString().length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(e.toString().getBytes());
            outputStream.close();
            AppServer.LOG("ERROR: " + e.toString());
        }

    }
}
