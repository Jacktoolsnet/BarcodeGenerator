package net.jacktools.barcode.barcodegenerator;

import com.google.zxing.BarcodeFormat;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Barcode;

import java.util.Optional;

public class MainViewController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView imageViewBarcode;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Alert alert = Assets.getAlert(Alert.AlertType.CONFIRMATION, true);
                alert.setHeaderText(Assets.getString("application.close.question.header"));
                alert.setContentText(Assets.getString("application.close.question.content"));
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.NO) {
                    e.consume();
                } else {
                    // Close the application.
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() {
        try {
            this.welcomeText.setText("978020137962");
            this.imageViewBarcode.setImage(Barcode.convertToFxImage(Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}