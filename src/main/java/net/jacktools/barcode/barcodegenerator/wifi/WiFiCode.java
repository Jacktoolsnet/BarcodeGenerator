/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.wifi;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WiFiCode {
    private final static String SEPARATOR = ";";
    private final static String WIFI_TAG = "WIFI:";
    private final static String T_TAG = "T:";
    private final static String S_TAG = "S:";
    private final static String P_TAG = "P:";
    private final static String H_TAG = "H:";
    public static String T;
    public static String S;
    public static String P;
    public static String H;
    public static Map<EncodeHintType, Object> HINTS = new HashMap<>();

    static {
        HINTS.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.toString());
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    }

    public static String getValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WIFI_TAG)
                .append(T_TAG)
                .append(T)
                .append(SEPARATOR)
                .append(S_TAG)
                .append(S)
                .append(SEPARATOR)
                .append(P_TAG)
                .append(P)
                .append(SEPARATOR)
                .append(H_TAG)
                .append(H)
                .append(SEPARATOR);
        return stringBuilder.toString();
    }
}
