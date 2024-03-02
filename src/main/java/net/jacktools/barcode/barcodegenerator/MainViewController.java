package net.jacktools.barcode.barcodegenerator;

import com.google.zxing.EncodeHintType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.epc.EpcCode;
import net.jacktools.barcode.barcodegenerator.epc.SupportedCurrency;
import net.jacktools.barcode.barcodegenerator.models.barcode.BarcodeTableCell;
import net.jacktools.barcode.barcodegenerator.models.barcode.BarcodeTableCellValue;
import net.jacktools.barcode.barcodegenerator.models.barcode.BarcodeTableView;
import net.jacktools.barcode.barcodegenerator.models.barcode.BarcodeTableViewDefinition;
import net.jacktools.barcode.barcodegenerator.models.epc.EpcTableCell;
import net.jacktools.barcode.barcodegenerator.models.epc.EpcTableCellValue;
import net.jacktools.barcode.barcodegenerator.models.epc.EpcTableView;
import net.jacktools.barcode.barcodegenerator.models.epc.EpcTableViewDefinition;
import net.jacktools.barcode.barcodegenerator.utils.*;
import net.jacktools.barcode.barcodegenerator.web.AppServer;
import net.jacktools.barcode.barcodegenerator.wifi.WiFiCode;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Level;

public class MainViewController {

    // Barcode TableView
    BarcodeTableView barcodeTableView;
    // EPC TableView
    EpcTableView epcTableView;
    NumberFormat integerFormat = NumberFormat.getIntegerInstance();
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
    private Accordion accordionQrCodes;
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
    private TableView<BarcodeTableViewDefinition> tableViewBarcode;
    @FXML
    private TableColumn<BarcodeTableViewDefinition, String> tableColumnBarcodeDesignation;
    @FXML
    private TableColumn<BarcodeTableViewDefinition, BarcodeTableCellValue> tableColumnBarcodeValue;
    @FXML
    private TableView<EpcTableViewDefinition> tableViewEpc;
    @FXML
    private TableColumn<EpcTableViewDefinition, String> tableColumnEpcDesignation;
    @FXML
    private TableColumn<EpcTableViewDefinition, EpcTableCellValue> tableColumnEpcValue;
    @FXML
    private Hyperlink hyperlinkPreview;
    @FXML
    private ImageView imageViewBarcode;
    @FXML
    private ImageView imageViewQrCode;
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
    private TitledPane titledPaneWifi;
    @FXML
    private TextField textFieldWiFiNetworkName;
    @FXML
    private PasswordField passwordFieldWiFiAuthentication;
    @FXML
    private CheckBox checkBoxWiFiHidden;
    @FXML
    private ChoiceBox<String> choiceBoxWiFiEncryption;
    @FXML
    private Spinner<Integer> spinnerWebServerPort;
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
    private TextField textFieldPayee;
    @FXML
    private TextField textFieldPurpose;
    @FXML
    private TextField textFieldPurposeOfUse;
    @FXML
    private TextField textFieldReference;
    private Stage stage;

    @FXML
    void initialize() {
        // Barcode
        this.tableColumnBarcodeDesignation.setCellValueFactory(new PropertyValueFactory<BarcodeTableViewDefinition, String>("designation"));
        this.tableColumnBarcodeValue.setCellValueFactory(cellData -> cellData.getValue().getTableCellValue());
        this.tableColumnBarcodeValue.setCellFactory((TableColumn<BarcodeTableViewDefinition, BarcodeTableCellValue> tableColumn) -> {
            return new BarcodeTableCell();
        });
        this.tableColumnBarcodeValue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BarcodeTableViewDefinition, BarcodeTableCellValue>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BarcodeTableViewDefinition, BarcodeTableCellValue> t) {
                createBarcode();
            }
        });
        this.barcodeTableView = new BarcodeTableView();
        this.tableViewBarcode.setItems(barcodeTableView.getObservableList());
        this.tableViewBarcode.refresh();
        // EPC
        this.tableColumnEpcDesignation.setCellValueFactory(new PropertyValueFactory<EpcTableViewDefinition, String>("designation"));
        this.tableColumnEpcValue.setCellValueFactory(cellData -> cellData.getValue().getTableCellValue());
        this.tableColumnEpcValue.setCellFactory((TableColumn<EpcTableViewDefinition, EpcTableCellValue> tableColumn) -> {
            return new EpcTableCell();
        });
        this.tableColumnEpcValue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<EpcTableViewDefinition, EpcTableCellValue>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<EpcTableViewDefinition, EpcTableCellValue> t) {
                createEpcBarcode();
            }
        });
        this.epcTableView = new EpcTableView();
        this.tableViewEpc.setItems(epcTableView.getObservableList());
        this.tableViewEpc.refresh();
        // WiFi
        this.choiceBoxWiFiEncryption.getItems().clear();
        this.choiceBoxWiFiEncryption.getItems().add("WEP");
        this.choiceBoxWiFiEncryption.getItems().add("WPA");
        this.choiceBoxWiFiEncryption.getItems().add("WPA-EAP");
        this.choiceBoxWiFiEncryption.getItems().add("WPA/WPA2");
    }

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
            this.createBarcode();
        });
        this.stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.createBarcode();
        });
        // Tabs
        this.tabBarcode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.createBarcode();
        });
        this.tabQrCode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.createPreviewHyperlink();
        });
        this.tabWebServer.selectedProperty().addListener((observable, oldValue, newValue) -> {
            this.createPreviewHyperlink();
        });
        // buttons
        this.buttonCopy.disableProperty().bind(this.tabPaneMain.getSelectionModel().selectedIndexProperty().greaterThan(1));
        this.buttonSave.disableProperty().bind(this.tabPaneMain.getSelectionModel().selectedIndexProperty().greaterThan(1));
        this.buttonWebServerStart.disableProperty().bind(AppServer.RUNNING);
        this.buttonWebServerStop.disableProperty().bind(AppServer.NOT_RUNNING);
        this.spinnerWebServerPort.disableProperty().bind(AppServer.RUNNING);
        this.spinnerWebServerPort.setEditable(true);
        this.spinnerWebServerPort.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        this.spinnerWebServerPort.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.WEB_SERVER_PORT_MIN, Settings.WEB_SERVER_PORT_MAX, Settings.WEB_SERVER_PORT));
        this.spinnerWebServerPort.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = spinnerWebServerPort.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerWebServerPort.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            this.saveWebserverSettings();
        });
        // checkbox
        this.checkBoxWebServerAutoStart.setSelected(Settings.WEB_SERVER_AUTOSTART);
        this.checkBoxWebServerAutoStart.selectedProperty().addListener((observable, oldValue, newValue) -> saveWebserverSettings());
        // Webserver log
        this.textAreaWebServerLog.textProperty().bind(AppServer.LOG_PROPERTY);
        // Accordion tabs
        this.titledPaneTransfer.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.createEpcBarcode();
                this.saveEpcSettings();
                this.createPreviewHyperlink();
            }
        });
        this.titledPaneWifi.expandedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.createWiFiBarcode();
                this.saveWiFiSettings();
                this.createPreviewHyperlink();
            }
        });
    }

    private void createBarcode() {
        Platform.runLater(() -> {
            try {
                if (!Settings.BARCODE_VALUE.isBlank() && null != Settings.BARCODE_TYPE) {
                    Map<EncodeHintType, Object> hints = new HashMap<>();
                    hints.put(EncodeHintType.MARGIN, Settings.BARCODE_DEFAULT_MARGIN);
                    imageViewBarcode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(Settings.BARCODE_VALUE, Settings.BARCODE_TYPE, Settings.BARCODE_DEFAULT_WIDTH, Settings.BARCODE_DEFAULT_HEIGHT, hints), Settings.BARCODE_BACKGROUND_COLOR, Settings.BARCODE_COLOR));
                    protectImageSize(this.imageViewBarcode);
                    this.createPreviewHyperlink();
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
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
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
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
        });
        this.textFieldPayee.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.PAYEE_CHECK)) {
                    this.textFieldPayee.setStyle("");
                } else {
                    this.textFieldPayee.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
            return c;
        }));
        this.textFieldIban.setText(Settings.IBAN);
        this.textFieldIban.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
        });
        this.textFieldIban.setTextFormatter(new TextFormatter<Object>(c -> {
            if (!c.getControlNewText().isBlank()) {
                if (c.getControlNewText().matches(EpcCode.IBAN_CHECK)) {
                    this.textFieldIban.setStyle("");
                } else {
                    this.textFieldIban.setStyle(null != Settings.APP_INVALID_VALUE_COLOR ? "-fx-control-inner-background: " + Settings.APP_INVALID_VALUE_COLOR : "");
                }
            }
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
            return c;
        }));
        this.textFieldPurpose.setText(Settings.PURPOSE);
        this.textFieldPurpose.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
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
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
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
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
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
            if (this.titledPaneTransfer.isExpanded()) {
                this.createEpcBarcode();
                this.saveEpcSettings();
            }
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
            Node increment = spinnerPaymentAmount.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = spinnerPaymentAmount.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
            if (this.titledPaneTransfer.isExpanded()) {
                this.saveEpcSettings();
            }
        });
    }

    public void setDefaultWiFiValues() {
        this.textFieldWiFiNetworkName.setText(Settings.S);
        this.textFieldWiFiNetworkName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneWifi.isExpanded()) {
                this.createWiFiBarcode();
                this.saveWiFiSettings();
            }
        });
        this.passwordFieldWiFiAuthentication.setText(Settings.P);
        this.passwordFieldWiFiAuthentication.textProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneWifi.isExpanded()) {
                this.createWiFiBarcode();
                this.saveWiFiSettings();
            }
        });
        this.choiceBoxWiFiEncryption.setValue(Settings.T);
        this.choiceBoxWiFiEncryption.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneWifi.isExpanded()) {
                this.createWiFiBarcode();
                this.saveWiFiSettings();
            }
        });
        this.checkBoxWiFiHidden.setSelected(Settings.H);
        this.checkBoxWiFiHidden.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (this.titledPaneWifi.isExpanded()) {
                this.createWiFiBarcode();
                this.saveWiFiSettings();
            }
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
                EpcCode.HINTS.put(EncodeHintType.MARGIN, Settings.QRCODE_DEFAULT_MARGIN);
                imageViewQrCode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(EpcCode.getValue(), SupportedBarcodeFormat.QR_CODE, Settings.QRCODE_DEFAULT_WIDTH, Settings.QRCODE_DEFAULT_HEIGHT, EpcCode.HINTS), Settings.QRCODE_BACKGROUND_COLOR, Settings.QRCODE_COLOR));
                protectImageSize(this.imageViewQrCode);
                this.createPreviewHyperlink();
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

    private void createWiFiBarcode() {
        Platform.runLater(() -> {
            try {
                WiFiCode.S = this.textFieldWiFiNetworkName.getText();
                WiFiCode.P = this.passwordFieldWiFiAuthentication.getText();
                WiFiCode.T = this.choiceBoxWiFiEncryption.getValue();
                WiFiCode.H = String.valueOf(this.checkBoxWiFiHidden.isSelected());
                WiFiCode.HINTS.put(EncodeHintType.MARGIN, Settings.QRCODE_DEFAULT_MARGIN);
                imageViewQrCode.setImage(Barcode.bufferedImageToFxImage(Barcode.create(WiFiCode.getValue(), SupportedBarcodeFormat.QR_CODE, Settings.QRCODE_DEFAULT_WIDTH, Settings.QRCODE_DEFAULT_HEIGHT, WiFiCode.HINTS), Settings.QRCODE_BACKGROUND_COLOR, Settings.QRCODE_COLOR));
                protectImageSize(this.imageViewQrCode);
                this.createPreviewHyperlink();
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

    private void saveWiFiSettings() {
        Settings.S = this.textFieldWiFiNetworkName.getText();
        Settings.P = this.passwordFieldWiFiAuthentication.getText();
        Settings.T = this.choiceBoxWiFiEncryption.getValue();
        Settings.H = this.checkBoxWiFiHidden.isSelected();
    }

    private void saveWebserverSettings() {
        Settings.WEB_SERVER_AUTOSTART = this.checkBoxWebServerAutoStart.isSelected();
        Settings.WEB_SERVER_PORT = this.spinnerWebServerPort.getValue();
    }

    private void protectImageSize(ImageView imageView) {
        imageView.setFitWidth(Settings.BARCODE_DEFAULT_WIDTH > this.stage.getWidth() - 100 ? this.stage.getWidth() - 100 : 0);
        imageView.setFitHeight(Settings.BARCODE_DEFAULT_HEIGHT > this.stage.getHeight() - 200 ? this.stage.getHeight() - 200 : 0);
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

    private void createPreviewHyperlink() {
        switch (this.tabPaneMain.getSelectionModel().getSelectedIndex()) {
            case 0:
                hyperlinkPreview.setText(Assets.getString("hyperlink.barcode", String.valueOf(Settings.WEB_SERVER_PORT), Settings.BARCODE_TYPE.getRoute(), URLEncoder.encode(Settings.BARCODE_VALUE, StandardCharsets.UTF_8), String.valueOf(Settings.BARCODE_DEFAULT_WIDTH), String.valueOf(Settings.BARCODE_DEFAULT_HEIGHT), Settings.BARCODE_COLOR, Settings.BARCODE_BACKGROUND_COLOR, Settings.BARCODE_DEFAULT_MARGIN));
                break;
            case 1:
                if (this.titledPaneTransfer.isExpanded()) {
                    hyperlinkPreview.setText(Assets.getString("hyperlink.barcode", String.valueOf(Settings.WEB_SERVER_PORT), SupportedBarcodeFormat.EPC.getRoute(), URLEncoder.encode(EpcCode.getValue(), StandardCharsets.UTF_8), String.valueOf(Settings.QRCODE_DEFAULT_WIDTH), String.valueOf(Settings.QRCODE_DEFAULT_HEIGHT), Settings.QRCODE_COLOR, Settings.QRCODE_BACKGROUND_COLOR, Settings.QRCODE_DEFAULT_MARGIN));
                } else if (this.titledPaneWifi.isExpanded()) {
                    hyperlinkPreview.setText(Assets.getString("hyperlink.barcode", String.valueOf(Settings.WEB_SERVER_PORT), SupportedBarcodeFormat.WIFI.getRoute(), URLEncoder.encode(WiFiCode.getValue(), StandardCharsets.UTF_8), String.valueOf(Settings.QRCODE_DEFAULT_WIDTH), String.valueOf(Settings.QRCODE_DEFAULT_HEIGHT), Settings.QRCODE_COLOR, Settings.QRCODE_BACKGROUND_COLOR, Settings.QRCODE_DEFAULT_MARGIN));
                } else {
                    hyperlinkPreview.setText("");
                }
                break;
            default:
                hyperlinkPreview.setText("");
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
        String helpPageUrl = "";
        switch (this.tabPaneMain.getSelectionModel().getSelectedIndex()) {
            case 0 -> {
                switch (Settings.BARCODE_TYPE) {
                    case AZTEC -> helpPageUrl = "https://de.wikipedia.org/wiki/Aztec-Code";
                    case CODE_39 -> helpPageUrl = "https://de.wikipedia.org/wiki/Code_39";
                    case CODE_93 -> helpPageUrl = "https://de.wikipedia.org/wiki/Strichcode";
                    case CODE_128 -> helpPageUrl = "https://de.wikipedia.org/wiki/Code128";
                    case CODABAR -> helpPageUrl = "https://de.wikipedia.org/wiki/Codabar";
                    case DATA_MATRIX -> helpPageUrl = "https://de.wikipedia.org/wiki/DataMatrix-Code";
                    case EAN_8 -> helpPageUrl = "https://de.wikipedia.org/wiki/European_Article_Number";
                    case EAN_13 -> helpPageUrl = "https://de.wikipedia.org/wiki/European_Article_Number";
                    case ITF -> helpPageUrl = "https://de.wikipedia.org/wiki/Strichcode";
                    case PDF_417 -> helpPageUrl = "https://de.wikipedia.org/wiki/PDF417";
                    case QR_CODE -> helpPageUrl = "https://de.wikipedia.org/wiki/QR-Code";
                    case UPC_A -> helpPageUrl = "https://de.wikipedia.org/wiki/Universal_Product_Code";
                    case UPC_E -> helpPageUrl = "https://de.wikipedia.org/wiki/Universal_Product_Code";
                    case EPC -> helpPageUrl = "https://de.wikipedia.org/wiki/EPC-QR-Code";
                    case WIFI -> helpPageUrl = "https://de.wikipedia.org/wiki/Strichcode";
                    default -> helpPageUrl = "https://de.wikipedia.org/wiki/Strichcode";
                }
            }
            case 1 -> {
                if (this.titledPaneTransfer.isExpanded()) {
                    helpPageUrl = "https://de.wikipedia.org/wiki/EPC-QR-Code";
                } else if (this.titledPaneWifi.isExpanded()) {
                    helpPageUrl = "https://de.wikipedia.org/wiki/Strichcode";
                } else {
                    helpPageUrl = "https://de.wikipedia.org/wiki/QR-Code";
                }
            }
            default -> helpPageUrl = "https://de.wikipedia.org/wiki/QR-Code";
        }
        if (!helpPageUrl.isBlank()) {
            try {
                Desktop.getDesktop().browse(new URI(helpPageUrl));
            } catch (IOException | URISyntaxException e) {
                AppLog.log(Level.SEVERE, e.getLocalizedMessage());
            }
        }
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
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(this.hyperlinkPreview.getText()));
            } else{
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(new String[]{"xdg-open", this.hyperlinkPreview.getText()});
            }
        } catch (Exception e) {
            AppLog.log(Level.SEVERE, e.getLocalizedMessage());
        }
    }
}