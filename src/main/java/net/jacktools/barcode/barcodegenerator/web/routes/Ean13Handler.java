package net.jacktools.barcode.barcodegenerator.web.routes;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class Ean13Handler implements HttpHandler {
    // Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150))
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            AppServer.LOG(httpExchange.getRequestURI().toString());
            Map<String, String> queryParameter = AppServer.queryToMap(httpExchange.getRequestURI().getQuery());
            if (null != queryParameter.get("value")) {
                byte[] bytes = Barcode.createByteArray(queryParameter.get("value"), SupportedBarcodeFormat.EAN_13, null != queryParameter.get("width") ? Integer.valueOf(queryParameter.get("width")) : Settings.BARCODE_DEFAULT_WIDTH, null != queryParameter.get("height") ? Integer.valueOf(queryParameter.get("height")) : Settings.BARCODE_DEFAULT_HEIGHT);
                httpExchange.sendResponseHeaders(200, bytes.length);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(bytes);
                outputStream.close();
                AppServer.LOG("OK");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, e.toString().length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(e.toString().getBytes());
            outputStream.close();
            AppServer.LOG("ERROR: " + e.toString());
        }

    }
}
