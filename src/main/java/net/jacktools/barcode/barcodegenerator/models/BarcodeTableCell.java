/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;

import java.util.Arrays;

public class BarcodeTableCell extends TableCell<TableViewDefinition, BarcodeTableCellValue> {

    private Label label = new Label();
    private TextField textField = new TextField();
    ;
    private ChoiceBox<SupportedBarcodeFormat> choiceBox = new ChoiceBox<>();
    private ColorPicker colorPicker = new ColorPicker();

    public BarcodeTableCell() {
        this.choiceBox.getItems().setAll(Arrays.asList(SupportedBarcodeFormat.values()));
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
                this.choiceBox.setValue(SupportedBarcodeFormat.valueOf(this.getItem().getTableCellValue()));
                setGraphic(this.choiceBox);
            }
            case TEXT -> {
                this.textField.setText(this.getItem().getTableCellValue());
                setGraphic(this.textField);
            }
            case NUMBER -> {
                ;
                this.textField.setText(this.getItem().getTableCellValue());
                setGraphic(this.textField);
            }
            case COLOR -> {
                this.colorPicker.setValue(Color.valueOf(this.getItem().getTableCellValue()));
                setGraphic(this.colorPicker);
            }
        }
    }

    @Override
    public void commitEdit(BarcodeTableCellValue s) {
        super.commitEdit(s);
        this.setGraphic(this.label);
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
