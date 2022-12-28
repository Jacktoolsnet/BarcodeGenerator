package net.jacktools.barcode.barcodegenerator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;

public class MainViewController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imageViewBarcode;

    @FXML
    protected void onHelloButtonClick() {
        try {
            this.welcomeText.setText("978020137962");
            this.imageViewBarcode.setImage(Barcode.convertToFxImage(Barcode.generateEAN13BarcodeImage(this.welcomeText.getText(), 300, 150)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}