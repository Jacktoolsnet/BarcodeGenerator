/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.barcode;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import net.jacktools.barcode.barcodegenerator.utils.SupportedBarcodeFormat;

public class BarcodeTableCellValue implements ObservableValue {
    private final BarcodeSettings setting;

    private final BarcodeSettingType settingType;

    private final SimpleStringProperty tableCellValue;

    public BarcodeTableCellValue(BarcodeSettings setting, BarcodeSettingType settingType, String tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(tableCellValue);
    }

    public BarcodeTableCellValue(BarcodeSettings setting, BarcodeSettingType settingType, SupportedBarcodeFormat tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(tableCellValue.toString());
    }

    public BarcodeTableCellValue(BarcodeSettings setting, BarcodeSettingType settingType, int tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(String.valueOf(tableCellValue));
    }

    public BarcodeTableCellValue(BarcodeSettings setting, BarcodeSettingType settingType, Color tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(tableCellValue.toString());
    }

    public BarcodeSettings getSetting() {
        return setting;
    }

    public BarcodeSettingType getSettingType() {
        return settingType;
    }

    public String getTableCellValue() {
        return tableCellValue.get();
    }

    public void setTableCellValue() {
        this.tableCellValue.set(tableCellValue.toString());
    }

    public SimpleStringProperty tableCellValueProperty() {
        return tableCellValue;
    }

    @Override
    public void addListener(ChangeListener changeListener) {

    }

    @Override
    public void removeListener(ChangeListener changeListener) {

    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {

    }
}
