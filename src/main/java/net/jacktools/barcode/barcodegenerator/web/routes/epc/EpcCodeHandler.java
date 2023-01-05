package net.jacktools.barcode.barcodegenerator.web.routes.epc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class EpcCodeHandler implements HttpHandler {
    // Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150))
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            AppServer.LOG(httpExchange.getRequestURI().toString());
            Map<String, String> queryParameter = null != httpExchange.getRequestURI().getQuery() ? AppServer.queryToMap(httpExchange.getRequestURI().getQuery()) : new HashMap<>();
            byte[] bytes = Barcode.createByteArray(null != queryParameter.get("value") ? queryParameter.get("value") : Settings.BARCODE_VALUE, SupportedBarcodeFormat.QR_CODE, null != queryParameter.get("width") ? Integer.valueOf(queryParameter.get("width")) : Settings.QRCODE_DEFAULT_WIDTH, null != queryParameter.get("height") ? Integer.valueOf(queryParameter.get("height")) : Settings.QRCODE_DEFAULT_HEIGHT, null != queryParameter.get("backgroundcolor") ? Color.valueOf(queryParameter.get("backgroundcolor")) : Settings.BARCODE_BACKGROUND_COLOR, null != queryParameter.get("color") ? Color.valueOf(queryParameter.get("color")) : Settings.BARCODE_COLOR);
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
            AppServer.LOG("ERROR: " + e);
        }

    }
}
