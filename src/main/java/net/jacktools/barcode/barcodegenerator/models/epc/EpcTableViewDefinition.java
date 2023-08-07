/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.epc;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class EpcTableViewDefinition {
    private final SimpleStringProperty designation;

    private final EpcTableCellValue tableCellValue;


    public EpcTableViewDefinition(String designation, EpcTableCellValue tableCellValue) {
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

    public ObservableValue<EpcTableCellValue> getTableCellValue() {
        return tableCellValue;
    }
}
