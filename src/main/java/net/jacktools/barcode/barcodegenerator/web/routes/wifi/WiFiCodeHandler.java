package net.jacktools.barcode.barcodegenerator.web.routes.wifi;

import com.google.zxing.EncodeHintType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.AppServer;
import net.jacktools.barcode.barcodegenerator.wifi.WiFiCode;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class WiFiCodeHandler implements HttpHandler {
    // Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150))
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            AppServer.LOG(httpExchange.getRequestURI().toString());
            Map<String, String> queryParameter = null != httpExchange.getRequestURI().getQuery() ? AppServer.queryToMap(httpExchange.getRequestURI().getQuery()) : new HashMap<>();
            WiFiCode.T = null != queryParameter.get("encryption") && !queryParameter.get("encryption").isBlank() ? queryParameter.get("encryption") : Settings.T;
            WiFiCode.S = null != queryParameter.get("ssid") && !queryParameter.get("ssid").isBlank() ? queryParameter.get("ssid") : Settings.S;
            WiFiCode.P = null != queryParameter.get("psk") && !queryParameter.get("psk").isBlank() ? queryParameter.get("psk") : Settings.P;
            WiFiCode.H = null != queryParameter.get("hidden") && !queryParameter.get("hidden").isBlank() ? String.valueOf(queryParameter.get("hidden")) : String.valueOf(Settings.H);
            WiFiCode.HINTS.put(EncodeHintType.MARGIN, null != queryParameter.get("margin") ? Integer.valueOf(queryParameter.get("margin")) : Settings.QRCODE_DEFAULT_MARGIN);
            byte[] bytes = Barcode.createByteArray(null != queryParameter.get("value") ? URLDecoder.decode(queryParameter.get("value")) : WiFiCode.getValue(), SupportedBarcodeFormat.QR_CODE, null != queryParameter.get("width") ? Integer.valueOf(queryParameter.get("width")) : Settings.QRCODE_DEFAULT_WIDTH, null != queryParameter.get("height") ? Integer.valueOf(queryParameter.get("height")) : Settings.QRCODE_DEFAULT_HEIGHT, null != queryParameter.get("backgroundcolor") ? Color.valueOf(queryParameter.get("backgroundcolor")) : Settings.BARCODE_BACKGROUND_COLOR, null != queryParameter.get("color") ? Color.valueOf(queryParameter.get("color")) : Settings.BARCODE_COLOR, WiFiCode.HINTS);
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
