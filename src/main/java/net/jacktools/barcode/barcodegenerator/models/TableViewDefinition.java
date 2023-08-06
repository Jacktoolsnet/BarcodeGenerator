package net.jacktools.barcode.barcodegenerator.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class TableViewDefinition {
    private final SimpleStringProperty designation;

    private final BarcodeTableCellValue tableCellValue;


    public TableViewDefinition(String designation, BarcodeTableCellValue tableCellValue) {
        this.designation = new SimpleStringProperty(designation);
        this.tableCellValue = tableCellValue;
    }

    public String getDesignation() {
        return designation.get();
    }

    public void setDesignation(String designation) {
        this.designation.set(designation);
    }

    public SimpleStringProperty designationProperty() {
        return designation;
    }

    public ObservableValue<BarcodeTableCellValue> getTableCellValue() {
        return tableCellValue;
    }
}
