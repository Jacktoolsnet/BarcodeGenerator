package net.jacktools.barcode.barcodegenerator.web.routes.epc;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.epc.EpcCode;
import net.jacktools.barcode.barcodegenerator.epc.SupportedCurrency;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class EpcCodeHandler implements HttpHandler {
    // Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150))
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            AppServer.LOG(httpExchange.getRequestURI().toString());
            Map<String, String> queryParameter = null != httpExchange.getRequestURI().getQuery() ? AppServer.queryToMap(httpExchange.getRequestURI().getQuery()) : new HashMap<>();
            EpcCode.BIC = null != queryParameter.get("bic") && !queryParameter.get("bic").isBlank() ? queryParameter.get("bic") : Settings.BIC;
            EpcCode.PAYEE = null != queryParameter.get("payee") && !queryParameter.get("payee").isBlank() ? queryParameter.get("payee") : Settings.PAYEE;
            EpcCode.IBAN = null != queryParameter.get("iban") && !queryParameter.get("iban").isBlank() ? queryParameter.get("iban") : Settings.IBAN;
            EpcCode.CURRENCY = null != queryParameter.get("currency") && !queryParameter.get("currency").isBlank() ? SupportedCurrency.valueOf(queryParameter.get("currency")) : Settings.CURRENCY;
            EpcCode.PAYMENT_AMOUNT = null != queryParameter.get("payment_amount") && !queryParameter.get("payment_amount").isBlank() ? Double.valueOf(queryParameter.get("payment_amount")) : Settings.PAYMENT_AMOUNT;
            EpcCode.PURPOSE = null != queryParameter.get("purpose") && !queryParameter.get("purpose").isBlank() ? queryParameter.get("purpose") : Settings.PURPOSE;
            EpcCode.REFERENCE = null != queryParameter.get("reference") && !queryParameter.get("reference").isBlank() ? queryParameter.get("reference") : Settings.REFERENCE;
            EpcCode.PURPOSE_OF_USE = null != queryParameter.get("purpose_of_use") && !queryParameter.get("purpose_of_use").isBlank() ? queryParameter.get("purpose_of_use") : Settings.PURPOSE_OF_USE;
            EpcCode.NOTICE = null != queryParameter.get("notice") && !queryParameter.get("notice").isBlank() ? queryParameter.get("notice") : Settings.NOTICE;
            byte[] bytes = Barcode.createByteArray(null != queryParameter.get("value") ? URLDecoder.decode(queryParameter.get("value")) : EpcCode.getValue(), SupportedBarcodeFormat.QR_CODE, null != queryParameter.get("width") ? Integer.valueOf(queryParameter.get("width")) : Settings.QRCODE_DEFAULT_WIDTH, null != queryParameter.get("height") ? Integer.valueOf(queryParameter.get("height")) : Settings.QRCODE_DEFAULT_HEIGHT, null != queryParameter.get("backgroundcolor") ? Color.valueOf(queryParameter.get("backgroundcolor")) : Settings.BARCODE_BACKGROUND_COLOR, null != queryParameter.get("color") ? Color.valueOf(queryParameter.get("color")) : Settings.BARCODE_COLOR, EpcCode.HINTS);
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
