/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.epc;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

public class EpcTableCellValue implements ObservableValue {
    private final EpcSettings setting;

    private final EpcSettingType settingType;

    private final SimpleStringProperty tableCellValue;

    public EpcTableCellValue(EpcSettings setting, EpcSettingType settingType, String tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(tableCellValue);
    }

    public EpcTableCellValue(EpcSettings setting, EpcSettingType settingType, int tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(String.valueOf(tableCellValue));
    }

    public EpcTableCellValue(EpcSettings setting, EpcSettingType settingType, Color tableCellValue) {
        this.setting = setting;
        this.settingType = settingType;
        this.tableCellValue = new SimpleStringProperty(tableCellValue.toString());
    }

    public EpcSettings getSetting() {
        return setting;
    }

    public EpcSettingType getSettingType() {
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
