package net.jacktools.barcode.barcodegenerator.epc;

public class EpcCode {
    public static final Double MIN_PAYMENT_AMOUNT = 0.01;
    public static final Double MAX_PAYMENT_AMOUNT = 999999999.99;
    private final static String LINE_SEPARATOR = "\r\n";
    private final static String ALLOWED_CHARS = "^[0-9A-ZÄÖÜßa-zäöü’:?,\\-(+.)/&*\\$%]+$";
    private final static String SERVICE_TAG = "BCD";
    private final static String VERSION = "002";
    private final static int CHARSET = 1;
    private final static String IDENTIFICATION = "SCT";
    public static String BIC;
    public static String PAYEE;
    public static String IBAN;
    public static SupportedCurrency CURRENCY;
    public static Double PAYMENT_AMOUNT;
    public static String PURPOSE;
    public static String REFERENCE;
    public static String PURPOSE_OF_USE;
    public static String NOTICE;

}
