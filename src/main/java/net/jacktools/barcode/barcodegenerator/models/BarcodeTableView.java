/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;

public class BarcodeTableView {
    final ObservableList<TableViewDefinition> observableList = FXCollections.observableArrayList();

    public BarcodeTableView() {
        super();
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.type"), new BarcodeTableCellValue(BarcodeSettings.TYPE, BarcodeSettingType.CHOICEBOX, Settings.BARCODE_TYPE)));
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.value"), new BarcodeTableCellValue(BarcodeSettings.VALUE, BarcodeSettingType.TEXT, Settings.BARCODE_VALUE)));
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.width"), new BarcodeTableCellValue(BarcodeSettings.WIDTH, BarcodeSettingType.NUMBER, Settings.BARCODE_DEFAULT_WIDTH)));
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.height"), new BarcodeTableCellValue(BarcodeSettings.HEIGHT, BarcodeSettingType.NUMBER, Settings.BARCODE_DEFAULT_HEIGHT)));
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.barcodecolor"), new BarcodeTableCellValue(BarcodeSettings.BARCODECOLOR, BarcodeSettingType.COLOR, Settings.BARCODE_COLOR)));
        this.observableList.add(new TableViewDefinition(Assets.getString("fxml.main.label.backgroundcolor"), new BarcodeTableCellValue(BarcodeSettings.BACKGROUNDCOLOR, BarcodeSettingType.COLOR, Settings.BARCODE_BACKGROUND_COLOR)));

        // this.observableList.add(new TableViewDefinition(BarcodeSettings.VALUE, Assets.getString("fxml.main.label.value"), Settings.BARCODE_VALUE));
        // this.observableList.add(new TableViewDefinition(BarcodeSettings.WIDTH, Assets.getString("fxml.main.label.width"), String.valueOf(Settings.BARCODE_DEFAULT_WIDTH)));
        // this.observableList.add(new TableViewDefinition(BarcodeSettings.HEIGHT, Assets.getString("fxml.main.label.height"), String.valueOf(Settings.BARCODE_DEFAULT_HEIGHT)));
        // this.observableList.add(new TableViewDefinition(BarcodeSettings.COLOR, Assets.getString("fxml.main.label.barcodecolor"), Settings.BARCODE_COLOR.toString()));
        // this.observableList.add(new TableViewDefinition(BarcodeSettings.BACKGROUND_COLOR, Assets.getString("fxml.main.label.backgroundcolor"), Settings.BARCODE_BACKGROUND_COLOR.toString()));
    }

    public ObservableList<TableViewDefinition> getObservableList() {
        return this.observableList;
    }
}
