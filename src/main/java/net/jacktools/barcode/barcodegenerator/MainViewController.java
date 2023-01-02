package net.jacktools.barcode.barcodegenerator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.jacktools.barcode.barcodegenerator.utils.AppLog;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

public class MainViewController {
    @FXML
    private Button buttonCloseApplication;

    @FXML
    private Button buttonCopy;

    @FXML
    private Button buttonInfo;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonWebServerStart;

    @FXML
    private Button buttonWebServerStop;

    @FXML
    private CheckBox checkBoxWebServerAutoStart;

    @FXML
    private ChoiceBox<SupportedBarcodeFormat> choiceBoxBarcodeType;

    @FXML
    private ColorPicker colorPickerBarcode;

    @FXML
    private ColorPicker colorPickerQrCode;

    @FXML
    private Hyperlink hyperlinkPreview;

    @FXML
    private ImageView imageViewBarcode;

    @FXML
    private ImageView imageViewQrCode;

    @FXML
    private Spinner<?> spinnerBarcodeHeight;

    @FXML
    private Spinner<?> spinnerBarcodeWidth;

    @FXML
    private Spinner<?> spinnerQrCodeHeigth;

    @FXML
    private Spinner<?> spinnerQrCodeWidth;

    @FXML
    private Spinner<?> spinnerWebServerPort;

    @FXML
    private Tab tabBarcode;

    @FXML
    private TabPane tabPaneMain;

    @FXML
    private Tab tabQrCode;

    @FXML
    private Tab tabWebServer;

    @FXML
    private TextArea textAreaWebServerLog;

    @FXML
    private TextField textFieldBarcodeValue;

    @FXML
    private TitledPane titledContact;

    @FXML
    private TitledPane titledPaneMeeting;

    @FXML
    private TitledPane titledPaneTransver;

    private Stage stage;

    @FXML
    void initialize() {
        choiceBoxBarcodeType.getItems().setAll(Arrays.asList(SupportedBarcodeFormat.values()));
    }

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
                    // Save settings.
                    Settings.saveProperties();
                    // Close the application.
                    AppLog.log(Level.INFO, Assets.getString("application.log.stop"));
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }

//    @FXML
//    protected void onHelloButtonClick() {
//        try {
//            this.welcomeText.setText("978020137962");
//            this.imageViewBarcode.setImage(Barcode.convertToFxImage(Barcode.create(this.welcomeText.getText(), BarcodeFormat.EAN_13, 300, 150)));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @FXML
    void buttonCloseApplication_onAction(ActionEvent event) {
        Alert alert = Assets.getAlert(Alert.AlertType.CONFIRMATION, true);
        alert.setHeaderText(Assets.getString("application.close.question.header"));
        alert.setContentText(Assets.getString("application.close.question.content"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            // Save settings.
            Settings.saveProperties();
            // Close the application.
            AppLog.log(Level.INFO, Assets.getString("application.log.stop"));
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    void buttonCopy_onAction(ActionEvent event) {

    }

    @FXML
    void buttonHelp_onAction(ActionEvent event) {

    }

    @FXML
    void buttonInfo_onAction(ActionEvent event) {

    }

    @FXML
    void buttonSave_onAction(ActionEvent event) {

    }

    @FXML
    void buttonWebServerStart_onAction(ActionEvent event) {

    }

    @FXML
    void buttonWebServerStop_onAction(ActionEvent event) {

    }

    @FXML
    void hyperlinkPreview_onAction(ActionEvent event) {

    }
}