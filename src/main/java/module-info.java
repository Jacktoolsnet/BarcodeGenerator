module net.jacktools.barcode.barcodegenerator {
    requires com.google.zxing.javase;
    requires com.google.zxing;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;
    requires java.logging;

    opens net.jacktools.barcode.barcodegenerator to javafx.fxml;
    exports net.jacktools.barcode.barcodegenerator;
}