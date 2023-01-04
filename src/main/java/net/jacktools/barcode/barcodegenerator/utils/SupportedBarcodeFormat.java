package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.BarcodeFormat;

public enum SupportedBarcodeFormat {
    AZTEC(BarcodeFormat.AZTEC),
    CODE_39(BarcodeFormat.CODE_39),
    CODE_93(BarcodeFormat.CODE_93),
    CODE_128(BarcodeFormat.CODE_128),
    CODABAR(BarcodeFormat.CODABAR),
    DATA_MATRIX(BarcodeFormat.DATA_MATRIX),
    EAN_8(BarcodeFormat.EAN_8),
    EAN_13(BarcodeFormat.EAN_13),
    ITF(BarcodeFormat.ITF),
    PDF_417(BarcodeFormat.PDF_417),
    QR_CODE(BarcodeFormat.QR_CODE),
    UPC_A(BarcodeFormat.UPC_A),
    UPC_E(BarcodeFormat.UPC_E);

    private BarcodeFormat barcodeFormat;

    SupportedBarcodeFormat(BarcodeFormat barcodeFormat) {
        this.barcodeFormat = barcodeFormat;
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.barcodeFormat;
    }

    public String toString() {
        return this.barcodeFormat.toString();
    }

}
