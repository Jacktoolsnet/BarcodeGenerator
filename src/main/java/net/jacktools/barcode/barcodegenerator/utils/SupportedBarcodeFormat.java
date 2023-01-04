package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.BarcodeFormat;

public enum SupportedBarcodeFormat {
    AZTEC(BarcodeFormat.AZTEC, "/aztec"),
    CODE_39(BarcodeFormat.CODE_39, "/code39"),
    CODE_93(BarcodeFormat.CODE_93, "/code93"),
    CODE_128(BarcodeFormat.CODE_128, "/code128"),
    CODABAR(BarcodeFormat.CODABAR, "/coebar"),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX, "/datamatrix"),
    EAN_8(BarcodeFormat.EAN_8, "/ean8"),
    EAN_13(BarcodeFormat.EAN_13, "/ean13"),
    ITF(BarcodeFormat.ITF, "/itf"),
    PDF_417(BarcodeFormat.PDF_417, "/pdf417"),
    QR_CODE(BarcodeFormat.QR_CODE, "/qrcode"),
    UPC_A(BarcodeFormat.UPC_A, "/upca"),
    UPC_E(BarcodeFormat.UPC_E, "/upce");

    private BarcodeFormat barcodeFormat;
    private String route;

    SupportedBarcodeFormat(BarcodeFormat barcodeFormat, String route) {
        this.barcodeFormat = barcodeFormat;
        this.route = route;
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.barcodeFormat;
    }

    public String getRoute() {
        return this.route;
    }

    public String toString() {
        return this.barcodeFormat.toString();
    }

}
