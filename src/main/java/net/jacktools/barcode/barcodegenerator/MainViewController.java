package net.jacktools.barcode.barcodegenerator;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.utils.*;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.awt.*;
import java.io.IOException;
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
    private ColorPicker colorPickerBarcodeBackground;

    @FXML
    private ColorPicker colorPickerQrCode;

    @FXML
    private ColorPicker colorPickerQrCodeBackground;

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
    private Spinner<Integer> spinnerQrCodeHeight;

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
            saveBarcodeSettings();
        });
        this.stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
            saveBarcodeSettings();
        });
        // buttons
        this.buttonCopy.disableProperty().bind(this.tabPaneMain.getSelectionModel().selectedIndexProperty().greaterThan(1));
        this.buttonSave.disableProperty().bind(this.tabPaneMain.getSelectionModel().selectedIndexProperty().greaterThan(1));
        this.buttonWebServerStart.disableProperty().bind(AppServer.RUNNING);
        this.buttonWebServerStop.disableProperty().bind(AppServer.NOT_RUNNING);
        // Barcode type
        this.choiceBoxBarcodeType.getSelectionModel().select(Settings.BARCODE_TYPE);
        this.choiceBoxBarcodeType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
            saveBarcodeSettings();
        });
        // Barcode value
        this.textFieldBarcodeValue.setText(Settings.BARCODE_VALUE);
        this.textFieldBarcodeValue.textProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
            saveBarcodeSettings();
        });
        // Barcode color
        this.colorPickerBarcode.setValue(Settings.BARCODE_COLOR);
        this.colorPickerBarcode.valueProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
            saveBarcodeSettings();
        });
        // Barcode Background color
        this.colorPickerBarcodeBackground.setValue(Settings.BARCODE_BACKGROUND_COLOR);
        this.colorPickerBarcodeBackground.valueProperty().addListener((observable, oldValue, newValue) -> {
            createBarcode();
            saveBarcodeSettings();
        });
        // QR-Code value
        // QR-Code color
        // QR-Code background color
        this.colorPickerQrCode.setValue(Settings.QRCODE_COLOR);
        this.colorPickerQrCodeBackground.setValue(Settings.QRCODE_BACKGROUND_COLOR);
        // Spinner
        this.spinnerBarcodeWidth.setEditable(true);
        this.spinnerBarcodeWidth.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerBarcodeWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.BARCODE_MIN_WIDTH, Settings.BARCODE_MAX_WIDTH, Settings.BARCODE_DEFAULT_WIDTH));
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
            saveBarcodeSettings();
        });
        this.spinnerBarcodeHeight.setEditable(true);
        this.spinnerBarcodeHeight.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerBarcodeHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.BARCODE_MIN_HEIGHT, Settings.BARCODE_MAX_HEIGHT, Settings.BARCODE_DEFAULT_HEIGHT));
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
            saveBarcodeSettings();
        });
        this.spinnerQrCodeWidth.setEditable(true);
        this.spinnerQrCodeWidth.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerQrCodeWidth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.QRCODE_MIN_WIDTH, Settings.QRCODE_MAX_WIDTH, Settings.QRCODE_DEFAULT_WIDTH));
        this.spinnerQrCodeWidth.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            createBarcode();
            saveBarcodeSettings();
        });
        this.spinnerQrCodeHeight.setEditable(true);
        this.spinnerQrCodeHeight.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerQrCodeHeight.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.QRCODE_MIN_HEIGHT, Settings.QRCODE_MAX_HEIGHT, Settings.QRCODE_DEFAULT_HEIGHT));
        this.spinnerQrCodeHeight.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            createBarcode();
            saveBarcodeSettings();
        });
        this.spinnerWebServerPort.disableProperty().bind(AppServer.RUNNING);
        this.spinnerWebServerPort.setEditable(true);
        this.spinnerWebServerPort.getEditor().setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter));
        this.spinnerWebServerPort.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.WEB_SERVER_PORT_MIN, Settings.WEB_SERVER_PORT_MAX, Settings.WEB_SERVER_PORT));
        this.spinnerWebServerPort.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            saveWebserverSettings();
        });
        // checkbox
        this.checkBoxWebServerAutoStart.setSelected(Settings.WEB_SERVER_AUTOSTART);
        this.checkBoxWebServerAutoStart.selectedProperty().addListener((observable, oldValue, newValue) -> {
            saveWebserverSettings();
        });
        // Webserver log
        this.textAreaWebServerLog.textProperty().bind(AppServer.LOG_PROPERTY);
    }

    private void createBarcode() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!textFieldBarcodeValue.getText().isBlank() && null != choiceBoxBarcodeType.getSelectionModel().getSelectedItem()) {
                        imageViewBarcode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(textFieldBarcodeValue.getText(), choiceBoxBarcodeType.getSelectionModel().getSelectedItem(), spinnerBarcodeWidth.getValue(), spinnerBarcodeHeight.getValue()), colorPickerBarcodeBackground.getValue(), colorPickerBarcode.getValue()));
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

    private void saveBarcodeSettings() {
        Settings.BARCODE_VALUE = this.textFieldBarcodeValue.getText();
        Settings.BARCODE_TYPE = this.choiceBoxBarcodeType.getValue();
        Settings.BARCODE_DEFAULT_WIDTH = this.spinnerBarcodeWidth.getValue();
        Settings.BARCODE_DEFAULT_HEIGHT = this.spinnerBarcodeWidth.getValue();
        Settings.BARCODE_COLOR = this.colorPickerBarcode.getValue();
        Settings.BARCODE_BACKGROUND_COLOR = this.colorPickerBarcodeBackground.getValue();
    }

    private void saveWebserverSettings() {
        Settings.WEB_SERVER_AUTOSTART = this.checkBoxWebServerAutoStart.isSelected();
        Settings.WEB_SERVER_PORT = this.spinnerWebServerPort.getValue();
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
        switch (this.tabPaneMain.getSelectionModel().getSelectedIndex()) {
            case 0 -> Barcode.toClipboard(this.imageViewBarcode.getImage());
            case 1 -> Barcode.toClipboard(this.imageViewQrCode.getImage());
        }
        if (null != Application.TRAY_ICON) {
            Application.TRAY_ICON.displayMessage(Assets.getString("application.title"), Assets.getString("application.tray.copytoclipboard"), TrayIcon.MessageType.INFO);
        }
    }

    @FXML
    void buttonHelp_onAction(ActionEvent event) {

    }

    @FXML
    void buttonInfo_onAction(ActionEvent event) {

    }

    @FXML
    void buttonSave_onAction(ActionEvent event) {
        try {
            switch (this.tabPaneMain.getSelectionModel().getSelectedIndex()) {
                case 0 -> Barcode.toFile(this.stage, this.imageViewBarcode.getImage());
                case 1 -> Barcode.toFile(this.stage, this.imageViewQrCode.getImage());
            }
        } catch (IOException e) {
            AppLog.log(Level.INFO, e.getLocalizedMessage());
        }
    }

    @FXML
    void buttonWebServerStart_onAction(ActionEvent event) {
        try {
            AppServer.start();
            AppServer.LOG(Assets.getString("application.tray.webserver.start", this.spinnerWebServerPort.getValue()));
            Application.TRAY_ICON.displayMessage(Assets.getString("application.title"), Assets.getString("application.tray.webserver.start", this.spinnerWebServerPort.getValue()), TrayIcon.MessageType.INFO);
        } catch (IOException e) {
            AppServer.LOG(Assets.getString("application.tray.webserver.error") + "\r\n" + e.getLocalizedMessage());
            Application.TRAY_ICON.displayMessage(Assets.getString("application.title"), Assets.getString("application.tray.webserver.error"), TrayIcon.MessageType.INFO);
        }
    }

    @FXML
    void buttonWebServerStop_onAction(ActionEvent event) {
        AppServer.stop();
        AppServer.LOG(Assets.getString("application.tray.webserver.stop"));
        Application.TRAY_ICON.displayMessage(Assets.getString("application.title"), Assets.getString("application.tray.webserver.stop"), TrayIcon.MessageType.INFO);
    }

    @FXML
    void hyperlinkPreview_onAction(ActionEvent event) {

    }
}