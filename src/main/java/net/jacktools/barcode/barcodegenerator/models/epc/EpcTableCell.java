/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.epc;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import net.jacktools.barcode.barcodegenerator.utils.Settings;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

public class EpcTableCell extends TableCell<EpcTableViewDefinition, EpcTableCellValue> {

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
    private final Label label = new Label();
    private final TextField textField = new TextField();
    private final Spinner<Integer> integerSpinner = new Spinner<>();
    private final ColorPicker colorPicker = new ColorPicker();

    public EpcTableCell() {
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
        this.integerSpinner.setEditable(true);
        this.integerSpinner.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        this.integerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 4096, 0));
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
                this.getItem().tableCellValueProperty().set(String.valueOf(this.integerSpinner.getEditor().getText()));
                commitEdit(this.getItem());
                event.consume();
            }
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
            case TEXT -> {
                this.textField.setText(this.getItem().getTableCellValue());
                setGraphic(this.textField);
            }
            case NUMBER -> {
                this.integerSpinner.getValueFactory().setValue(Integer.valueOf(this.getItem().getTableCellValue()));
                switch (this.getItem().getSetting()) {
                    case WIDTH -> {
                        this.integerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.QRCODE_MIN_WIDTH, Settings.QRCODE_MAX_WIDTH, Settings.QRCODE_DEFAULT_WIDTH));
                    }
                    case HEIGHT -> {
                        this.integerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Settings.QRCODE_MIN_HEIGHT, Settings.QRCODE_MAX_HEIGHT, Settings.QRCODE_DEFAULT_HEIGHT));
                    }
                    case MARGIN -> {
                        this.integerSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, Settings.QRCODE_DEFAULT_MARGIN));
                    }
                }
                setGraphic(this.integerSpinner);
            }
            case COLOR -> {
                this.colorPicker.setValue(Color.valueOf(this.getItem().getTableCellValue()));
                setGraphic(this.colorPicker);
            }
        }
    }

    @Override
    public void commitEdit(EpcTableCellValue value) {
        this.setText(value.getTableCellValue());
        this.label.setText(value.getTableCellValue());
        this.setGraphic(this.label);
        switch (this.getItem().getSetting()) {
            case WIDTH -> {
                Settings.QRCODE_DEFAULT_WIDTH = Integer.valueOf(this.label.getText());
            }
            case HEIGHT -> {
                Settings.QRCODE_DEFAULT_HEIGHT = Integer.valueOf(this.label.getText());
            }
            case MARGIN -> {
                Settings.QRCODE_DEFAULT_MARGIN = Integer.valueOf(this.label.getText());
            }
            case QRCODECOLOR -> {
                Settings.QRCODE_COLOR = Color.valueOf(this.label.getText());
            }
            case BACKGROUNDCOLOR -> {
                Settings.QRCODE_BACKGROUND_COLOR = Color.valueOf(this.label.getText());
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
    protected void updateItem(EpcTableCellValue value, boolean empty) {
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
