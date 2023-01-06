package net.jacktools.barcode.barcodegenerator.epc;

public class EpcCode {
    public static final Double MIN_PAYMENT_AMOUNT = 0.01;
    public static final Double MAX_PAYMENT_AMOUNT = 999999999.99;
    private final static String LINE_SEPARATOR = "\r\n";
    public final static String BIC_CHECK = "^[A-Z0-9]{4}(?:AD|AE|AF|AG|AI|AL|AM|AO|AQ|AR|AS|AT|AU|AW|AX|AZ|BA|BB|BD|BE|BF|BG|BH|BI|BJ|BL|BM|BN|BO|BQ|BR|BS|BT|BV|BW|BY|BZ|CA|CC|CD|CF|CG|CH|CI|CK|CL|CM|CN|CO|CR|CU|CV|CW|CX|CY|CZ|DE|DJ|DK|DM|DO|DZ|EC|EE|EG|EH|ER|ES|ET|FI|FJ|FK|FM|FO|FR|GA|GB|GD|GE|GF|GG|GH|GI|GL|GM|GN|GP|GQ|GR|GS|GT|GU|GW|GY|HK|HM|HN|HR|HT|HU|ID|IE|IL|IM|IN|IO|IQ|IR|IS|IT|JE|JM|JO|JP|KE|KG|KH|KI|KM|KN|KP|KR|KW|KY|KZ|LA|LB|LC|LI|LK|LR|LS|LT|LU|LV|LY|MA|MC|MD|ME|MF|MG|MH|MK|ML|MM|MN|MO|MP|MQ|MR|MS|MT|MU|MV|MW|MX|MY|MZ|NA|NC|NE|NF|NG|NI|NL|NO|NP|NR|NU|NZ|OM|PA|PE|PF|PG|PH|PK|PL|PM|PN|PR|PS|PT|PW|PY|QA|RE|RO|RS|RU|RW|SA|SB|SC|SD|SE|SG|SH|SI|SJ|SK|SL|SM|SN|SO|SR|SS|ST|SV|SX|SY|SZ|TC|TD|TF|TG|TH|TJ|TK|TL|TM|TN|TO|TR|TT|TV|TW|TZ|UA|UG|UM|US|UY|UZ|VA|VC|VE|VG|VI|VN|VU|WF|WS|YE|YT|ZA|ZM|ZW)[A-Z0-9]{2}(?:[A-Z0-9]{3})?$";
    private final static String SERVICE_TAG = "BCD";
    private final static String VERSION = "002";
    private final static int CHARSET = 1;
    private final static String IDENTIFICATION = "SCT";
    public static String BIC;
    private final static String ALLOWED_CHARS = "[0-9A-ZÄÖÜßa-zäöü’:?,\\-(+.)/&*\\$% ]";
    public static String PAYEE;
    public static String PAYEE_CHECK = "^" + ALLOWED_CHARS + "{0,70}$";
    public static String IBAN;
    public static SupportedCurrency CURRENCY;
    public static Double PAYMENT_AMOUNT;
    public static String PURPOSE;
    public static String PURPOSE_CHECK = "^" + ALLOWED_CHARS + "{0,4}$";
    public static String REFERENCE;
    public static String REFERENCE_CHECK = "^" + ALLOWED_CHARS + "{0,25}$";
    public static String PURPOSE_OF_USE;
    public static String PURPOSE_OF_USE_CHECK = "^" + ALLOWED_CHARS + "{0,140}$";
    public static String NOTICE;
    public static String NOTICE_CHECK = "^" + ALLOWED_CHARS + "{0,70}$";

}
