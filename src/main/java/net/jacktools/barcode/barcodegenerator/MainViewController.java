package net.jacktools.barcode.barcodegenerator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.utils.*;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.UnaryOperator;
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

    NumberFormat format = NumberFormat.getIntegerInstance();
    UnaryOperator<TextFormatter.Change> filter = c -> {
        if (c.isContentChange()) {
            ParsePosition parsePosition = new ParsePosition(0);
            // NumberFormat evaluates the beginning of the text
            format.parse(c.getControlNewText(), parsePosition);
            if (parsePosition.getIndex() == 0 ||
                    parsePosition.getIndex() < c.getControlNewText().length()) {
                // reject parsing the complete text failed
                return null;
            }
        }
        return c;
    };
    @FXML
    private Spinner<Integer> spinnerBarcodeHeight;
    @FXML
    private Spinner<Integer> spinnerBarcodeWidth;
    @FXML
    private Spinner<Integer> spinnerQrCodeHeigth;

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
    private Spinner<Integer> spinnerQrCodeWidth;
    @FXML
    private Spinner<Integer> spinnerWebServerPort;

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

    public void setDefaultValues() {
        // stage
        this.stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
        });
        this.stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
        });
        // Barcode type
        this.choiceBoxBarcodeType.getSelectionModel().select(SupportedBarcodeFormat.QR_CODE);
        this.choiceBoxBarcodeType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
        });
        // Barcode value
        this.textFieldBarcodeValue.textProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
        });
        // Barcode color
        this.colorPickerBarcode.valueProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
        });
        // Spinner
        this.spinnerBarcodeWidth.setEditable(true);
        this.spinnerBarcodeWidth.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerBarcodeWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, Settings.BARCODE_DEFAULT_WIDTH));
        this.spinnerBarcodeWidth.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            createBarcode();
        });
        this.spinnerBarcodeHeight.setEditable(true);
        this.spinnerBarcodeHeight.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerBarcodeHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, Settings.BARCODE_DEFAULT_HEIGHT));
        this.spinnerBarcodeHeight.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            createBarcode();
        });
        this.spinnerQrCodeWidth.setEditable(true);
        this.spinnerQrCodeWidth.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerQrCodeWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(16, 2048, Settings.BARCODE_DEFAULT_WIDTH));
        this.spinnerQrCodeHeigth.setEditable(true);
        this.spinnerQrCodeHeigth.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerQrCodeHeigth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(16, 2048, Settings.BARCODE_DEFAULT_HEIGHT));
        this.spinnerWebServerPort.setEditable(true);
        this.spinnerWebServerPort.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerWebServerPort.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 65535, Settings.WEB_SERVER_PORT));
    }

    private void createBarcode() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!textFieldBarcodeValue.getText().isBlank() && null != choiceBoxBarcodeType.getSelectionModel().getSelectedItem()) {
                        imageViewBarcode.setImage(Barcode.convertToFxImage(Barcode.create(textFieldBarcodeValue.getText(), choiceBoxBarcodeType.getSelectionModel().getSelectedItem(), spinnerBarcodeWidth.getValue(), spinnerBarcodeHeight.getValue()), Color.WHITE, colorPickerBarcode.getValue()));
                        protectImageSize();
                    }
                } catch (Exception exception) {
                    Alert alert = Assets.getAlert(Alert.AlertType.INFORMATION, true);
                    alert.setHeaderText(Assets.getString("application.error.header"));
                    alert.setContentText(exception.getLocalizedMessage());
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(ButtonType.OK);
                    alert.show();
                }
            }
        });

    }

    private void protectImageSize() {
        this.imageViewBarcode.setFitWidth(this.spinnerBarcodeWidth.getValue() > this.stage.getWidth() - 100 ? this.stage.getWidth() - 100 : 0);
        this.imageViewBarcode.setFitHeight(this.spinnerBarcodeHeight.getValue() > this.stage.getHeight() - 200 ? this.stage.getHeight() - 200 : 0);
    }

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