package net.jacktools.barcode.barcodegenerator;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.epc.EpcCode;
import net.jacktools.barcode.barcodegenerator.epc.SupportedCurrency;
import net.jacktools.barcode.barcodegenerator.utils.*;
import net.jacktools.barcode.barcodegenerator.web.AppServer;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
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

    NumberFormat integerFormat = NumberFormat.getIntegerInstance();

    @FXML
    private Spinner<Integer> spinnerQrCodeWidth;

    @FXML
    private Spinner<Integer> spinnerWebServerPort;
    UnaryOperator<TextFormatter.Change> integerFilter = c -> {
        if (c.isContentChange()) {
            ParsePosition parsePosition = new ParsePosition(0);
            // NumberFormat evaluates the beginning of the text
            integerFormat.parse(c.getControlNewText(), parsePosition);
            if (parsePosition.getIndex() == 0 ||
                    parsePosition.getIndex() < c.getControlNewText().length()) {
                // reject parsing the complete text failed
                return null;
            }
        }
        return c;
    };
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
    UnaryOperator<TextFormatter.Change> currencyFilter = c -> {
        if (c.isContentChange()) {
            ParsePosition parsePosition = new ParsePosition(0);
            // NumberFormat evaluates the beginning of the text
            currencyFormat.parse(c.getControlNewText(), parsePosition);
            if (parsePosition.getIndex() == 0 ||
                    parsePosition.getIndex() < c.getControlNewText().length()) {
                // reject parsing the complete text failed
                return null;
            }
        }
        return c;
    };
    @FXML
    private TitledPane titledPaneTransfer;
    @FXML
    private Spinner<Double> spinnerPaymentAmount;
    @FXML
    private TextField textFieldBic;
    @FXML
    private TextField textFieldIban;
    @FXML
    private TextField textFieldNotice;

    @FXML
    void initialize() {
        choiceBoxBarcodeType.getItems().setAll(Arrays.asList(SupportedBarcodeFormat.values()));
    }

    @FXML
    private TextField textFieldPayee;
    @FXML
    private TextField textFieldPurpose;
    @FXML
    private TextField textFieldPurposeOfUse;
    @FXML
    private TextField textFieldReference;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(e -> {
            Alert alert = Assets.getAlert(Alert.AlertType.CONFIRMATION, true, stage);
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
        // QR-Code color
        this.colorPickerQrCode.setValue(Settings.QRCODE_COLOR);
        // QR-Code background color
        this.colorPickerQrCodeBackground.setValue(Settings.QRCODE_BACKGROUND_COLOR);
        // Spinner
        this.spinnerBarcodeWidth.setEditable(true);
        this.spinnerBarcodeWidth.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
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
        this.spinnerBarcodeHeight.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
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
        this.spinnerQrCodeWidth.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
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
        this.spinnerQrCodeHeight.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
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
        this.spinnerWebServerPort.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
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
        this.checkBoxWebServerAutoStart.selectedProperty().addListener((observable, oldValue, newValue) -> saveWebserverSettings());
        // Webserver log
        this.textAreaWebServerLog.textProperty().bind(AppServer.LOG_PROPERTY);
    }

    private void createBarcode() {
        Platform.runLater(() -> {
            try {
                if (!textFieldBarcodeValue.getText().isBlank() && null != choiceBoxBarcodeType.getSelectionModel().getSelectedItem()) {
                    imageViewBarcode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(textFieldBarcodeValue.getText(), choiceBoxBarcodeType.getSelectionModel().getSelectedItem(), spinnerBarcodeWidth.getValue(), spinnerBarcodeHeight.getValue()), colorPickerBarcodeBackground.getValue(), colorPickerBarcode.getValue()));
                    protectImageSize(this.imageViewBarcode);
                    hyperlinkPreview.setText(Assets.getString("hyperlink.barcode", String.valueOf(Settings.WEB_SERVER_PORT), choiceBoxBarcodeType.getSelectionModel().getSelectedItem().getRoute(), URLEncoder.encode(textFieldBarcodeValue.getText(), StandardCharsets.UTF_8), String.valueOf(spinnerBarcodeWidth.getValue()), String.valueOf(spinnerBarcodeHeight.getValue()), colorPickerBarcode.getValue().toString(), colorPickerBarcodeBackground.getValue().toString()));
                }
            } catch (Exception exception) {
                Alert alert = Assets.getAlert(Alert.AlertType.INFORMATION, true, stage);
                alert.setHeaderText(Assets.getString("application.error.header"));
                alert.setContentText(exception.getLocalizedMessage());
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.OK);
                alert.show();
            }
        });
    }

    public void setDefaultEpcValues() {
        this.textFieldBic.setText(Settings.BIC);
        this.textFieldBic.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldBic.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().toUpperCase(Locale.ENGLISH).matches(EpcCode.BIC_CHECK)) {
                    this.textFieldBic.setStyle("");
                } else {
                    this.textFieldBic.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            return c;
        }));
        this.textFieldPayee.setText(Settings.PAYEE);
        this.textFieldPayee.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldPayee.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.PAYEE_CHECK)) {
                    this.textFieldPayee.setStyle("");
                } else {
                    this.textFieldPayee.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            createEpcBarcode();
            saveEpcSettings();
            return c;
        }));
        this.textFieldIban.setText(Settings.IBAN);
        this.textFieldIban.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldIban.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.IBAN_CHECK)) {
                    this.textFieldIban.setStyle("");
                } else {
                    this.textFieldIban.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            createEpcBarcode();
            saveEpcSettings();
            return c;
        }));
        this.textFieldPurpose.setText(Settings.PURPOSE);
        this.textFieldPurpose.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldPurpose.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.PURPOSE_CHECK)) {
                    this.textFieldPurpose.setStyle("");
                } else {
                    this.textFieldPurpose.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            return c;
        }));
        this.textFieldReference.setText(Settings.REFERENCE);
        this.textFieldReference.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldReference.disableProperty().bind(this.textFieldPurposeOfUse.textProperty().isNotEqualTo(""));
        this.textFieldReference.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.REFERENCE_CHECK)) {
                    this.textFieldReference.setStyle("");
                } else {
                    this.textFieldReference.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            return c;
        }));
        this.textFieldPurposeOfUse.setText(Settings.PURPOSE_OF_USE);
        this.textFieldPurposeOfUse.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldPurposeOfUse.disableProperty().bind(this.textFieldReference.textProperty().isNotEqualTo(""));
        this.textFieldPurposeOfUse.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.PURPOSE_OF_USE_CHECK)) {
                    this.textFieldPurposeOfUse.setStyle("");
                } else {
                    this.textFieldPurposeOfUse.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            return c;
        }));
        this.textFieldNotice.setText(Settings.NOTICE);
        this.textFieldNotice.textProperty().addListener((observable, oldValue, newValue) -> {
            createEpcBarcode();
            saveEpcSettings();
        });
        this.textFieldNotice.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.NOTICE_CHECK)) {
                    this.textFieldNotice.setStyle("");
                } else {
                    this.textFieldNotice.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            return c;
        }));
        //  Spinner
        this.spinnerPaymentAmount.setEditable(true);
        SpinnerValueFactory spinnerPaymentAmountFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(EpcCode.MIN_PAYMENT_AMOUNT, EpcCode.MAX_PAYMENT_AMOUNT, null != Settings.PAYMENT_AMOUNT ? Settings.PAYMENT_AMOUNT : 0.00, 1.00);
        spinnerPaymentAmountFactory.setConverter(new CurrencyStringConverter());
        this.spinnerPaymentAmount.setValueFactory(spinnerPaymentAmountFactory);
        this.spinnerPaymentAmount.getEditor().setTextFormatter(new TextFormatter<>(new CurrencyStringConverter(), Settings.PAYMENT_AMOUNT, currencyFilter));
        this.spinnerPaymentAmount.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerBarcodeWidth.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerBarcodeWidth.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            createEpcBarcode();
            saveEpcSettings();
        });
    }

    private void createEpcBarcode() {
        Platform.runLater(() -> {
            try {
                EpcCode.BIC = this.textFieldBic.getText();
                EpcCode.PAYEE = this.textFieldPayee.getText();
                EpcCode.IBAN = this.textFieldIban.getText();
                EpcCode.CURRENCY = SupportedCurrency.EUR;
                EpcCode.PAYMENT_AMOUNT = this.spinnerPaymentAmount.getValue();
                EpcCode.PURPOSE = this.textFieldPurpose.getText();
                EpcCode.REFERENCE = this.textFieldReference.getText();
                EpcCode.PURPOSE_OF_USE = this.textFieldPurposeOfUse.getText();
                EpcCode.NOTICE = this.textFieldNotice.getText();
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.toString());
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                imageViewQrCode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(EpcCode.getValue(), SupportedBarcodeFormat.QR_CODE, spinnerQrCodeWidth.getValue(), spinnerQrCodeHeight.getValue(), EpcCode.HINTS), colorPickerQrCodeBackground.getValue(), colorPickerQrCode.getValue()));
                protectImageSize(this.imageViewQrCode);
                hyperlinkPreview.setText(Assets.getString("hyperlink.barcode", String.valueOf(Settings.WEB_SERVER_PORT), SupportedBarcodeFormat.EPC_CODE.getRoute(), URLEncoder.encode(EpcCode.getValue(), StandardCharsets.UTF_8), String.valueOf(spinnerBarcodeWidth.getValue()), String.valueOf(spinnerBarcodeHeight.getValue()), colorPickerBarcode.getValue().toString(), colorPickerBarcodeBackground.getValue().toString()));
            } catch (Exception exception) {
                Alert alert = Assets.getAlert(Alert.AlertType.INFORMATION, true, stage);
                alert.setHeaderText(Assets.getString("application.error.header"));
                alert.setContentText(exception.getLocalizedMessage());
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.OK);
                alert.show();
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

    private void saveEpcSettings() {
        Settings.BIC = this.textFieldBic.getText();
        Settings.PAYEE = this.textFieldPayee.getText();
        Settings.IBAN = this.textFieldIban.getText();
        Settings.CURRENCY = SupportedCurrency.EUR;
        Settings.PAYMENT_AMOUNT = this.spinnerPaymentAmount.getValue();
        Settings.PURPOSE = this.textFieldPurpose.getText();
        Settings.REFERENCE = this.textFieldReference.getText();
        Settings.PURPOSE_OF_USE = this.textFieldPurposeOfUse.getText();
        Settings.NOTICE = this.textFieldNotice.getText();
    }

    private void saveWebserverSettings() {
        Settings.WEB_SERVER_AUTOSTART = this.checkBoxWebServerAutoStart.isSelected();
        Settings.WEB_SERVER_PORT = this.spinnerWebServerPort.getValue();
    }

    private void protectImageSize(ImageView imageView) {
        imageView.setFitWidth(this.spinnerBarcodeWidth.getValue() > this.stage.getWidth() - 100 ? this.stage.getWidth() - 100 : 0);
        imageView.setFitHeight(this.spinnerBarcodeHeight.getValue() > this.stage.getHeight() - 200 ? this.stage.getHeight() - 200 : 0);
    }

    @FXML
    void buttonCloseApplication_onAction(ActionEvent event) {
        Alert alert = Assets.getAlert(Alert.AlertType.CONFIRMATION, true, this.stage);
        alert.setHeaderText(Assets.getString("application.close.question.header"));
        alert.setContentText(Assets.getString("application.close.question.content"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
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
        try {
            Desktop.getDesktop().browse(new URL(this.hyperlinkPreview.getText()).toURI());
        } catch (IOException | URISyntaxException e) {
            AppLog.log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
}