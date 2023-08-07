package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.BarcodeFormat;

public enum SupportedBarcodeFormat {
    AZTEC(BarcodeFormat.AZTEC, "AZTEC", "/aztec"),
    CODE_39(BarcodeFormat.CODE_39, "CODE 39", "/code39"),
    CODE_93(BarcodeFormat.CODE_93, "CODE 93", "/code93"),
    CODE_128(BarcodeFormat.CODE_128, "CODE 128", "/code128"),
    CODABAR(BarcodeFormat.CODABAR, "CODEBAR", "/coebar"),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX, "DATA MATRIX", "/datamatrix"),
    EAN_8(BarcodeFormat.EAN_8, "EAN 8", "/ean8"),
    EAN_13(BarcodeFormat.EAN_13, "EAN 13", "/ean13"),
    ITF(BarcodeFormat.ITF, "TIF", "/itf"),
    PDF_417(BarcodeFormat.PDF_417, "PDF 417", "/pdf417"),
    QR_CODE(BarcodeFormat.QR_CODE, "QR CODE", "/qrcode"),
    UPC_A(BarcodeFormat.UPC_A, "UPC A", "/upca"),
    UPC_E(BarcodeFormat.UPC_E, "UPC E", "/upce"),
    EPC_CODE(BarcodeFormat.QR_CODE, "GIROCODE", "/girocode");

    private final BarcodeFormat barcodeFormat;
    private final String barcodeName;
    private final String route;

    SupportedBarcodeFormat(BarcodeFormat barcodeFormat, String barcodeName, String route) {
        this.barcodeFormat = barcodeFormat;
        this.barcodeName = barcodeName;
        this.route = route;
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.barcodeFormat;
    }

    public String getBarcodeName() {
        return this.barcodeName;
    }

    public String getRoute() {
        return this.route;
    }

    public String toString() {
        return this.barcodeName;
    }

}
