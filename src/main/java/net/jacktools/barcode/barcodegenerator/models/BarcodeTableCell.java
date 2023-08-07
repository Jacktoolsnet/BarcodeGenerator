/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.utils.Settings;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class BarcodeTableCell extends TableCell<TableViewDefinition, BarcodeTableCellValue> {

    private Label label = new Label();
    private TextField textField = new TextField();

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
    private Spinner<Integer> integerSpinner = new Spinner<>();
    private ChoiceBox<SupportedBarcodeFormat> choiceBox = new ChoiceBox<>();
    private ColorPicker colorPicker = new ColorPicker();

    public BarcodeTableCell() {
        this.textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                this.getItem().tableCellValueProperty().set(this.textField.getText());
                commitEdit(this.getItem());
                event.consume();
            }
        });
        this.integerSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        this.integerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.BARCODE_MIN_WIDTH, Settings.BARCODE_MAX_WIDTH, Settings.BARCODE_DEFAULT_WIDTH));
        this.integerSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node increment = integerSpinner.lookup(".increment-arrow-button");
            if (increment != null) {
                increment.getOnMouseReleased().handle(null);
            }

            Node decrement = integerSpinner.lookup(".decrement-arrow-button");
            if (decrement != null) {
                decrement.getOnMouseReleased().handle(null);
            }
        });
        this.integerSpinner.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                this.getItem().tableCellValueProperty().set(String.valueOf(this.integerSpinner.getValue()));
                commitEdit(this.getItem());
                event.consume();
            }
        });
        this.choiceBox.getItems().setAll(Arrays.asList(SupportedBarcodeFormat.values()));
        this.choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.getItem().tableCellValueProperty().set(this.choiceBox.getValue().toString());
            commitEdit(this.getItem());
        });
        this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.getItem().tableCellValueProperty().set(this.colorPicker.getValue().toString());
            commitEdit(this.getItem());
        });
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        if (null != this.getItem()) {
            this.label.setText(this.getItem().getTableCellValue());
            this.setGraphic(this.label);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        switch (this.getItem().getSettingType()) {
            case CHOICEBOX -> {
                this.choiceBox.getSelectionModel().select(Settings.BARCODE_TYPE);
                setGraphic(this.choiceBox);
            }
            case TEXT -> {
                this.textField.setText(this.getItem().getTableCellValue());
                setGraphic(this.textField);
            }
            case NUMBER -> {
                this.integerSpinner.getValueFactory().setValue(Integer.valueOf(this.getItem().getTableCellValue()));
                setGraphic(this.integerSpinner);
            }
            case COLOR -> {
                this.colorPicker.setValue(Color.valueOf(this.getItem().getTableCellValue()));
                setGraphic(this.colorPicker);
            }
        }
    }

    @Override
    public void commitEdit(BarcodeTableCellValue value) {
        this.setText(value.getTableCellValue());
        this.label.setText(value.getTableCellValue());
        this.setGraphic(this.label);
        switch (this.getItem().getSetting()) {
            case TYPE -> {
                Settings.BARCODE_TYPE = this.choiceBox.getValue();
            }
            case VALUE -> {
                Settings.BARCODE_VALUE = this.textField.getText();
            }
            case WIDTH -> {
                Settings.BARCODE_DEFAULT_WIDTH = this.integerSpinner.getValue();
            }
            case HEIGHT -> {
                Settings.BARCODE_DEFAULT_HEIGHT = this.integerSpinner.getValue();
            }
            case BARCODECOLOR -> {
                Settings.BARCODE_COLOR = this.colorPicker.getValue();
            }
            case BACKGROUNDCOLOR -> {
                Settings.BARCODE_BACKGROUND_COLOR = this.colorPicker.getValue();
            }
        }
        super.commitEdit(value);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        this.setGraphic(this.label);
    }

    @Override
    protected void updateItem(BarcodeTableCellValue value, boolean empty) {
        super.updateItem(value, empty);
        if (null != value && !empty) {
            this.setText(value.getTableCellValue());
            this.label.setText(value.getTableCellValue());
            setGraphic(this.label);
        } else {
            this.setText("");
            setGraphic(null);
        }
    }

}
