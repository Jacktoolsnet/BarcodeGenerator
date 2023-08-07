/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.barcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;

public class BarcodeTableView {
    final ObservableList<BarcodeTableViewDefinition> observableList = FXCollections.observableArrayList();

    public BarcodeTableView() {
        super();
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.type"), new BarcodeTableCellValue(BarcodeSettings.TYPE, BarcodeSettingType.CHOICEBOX, Settings.BARCODE_TYPE)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.value"), new BarcodeTableCellValue(BarcodeSettings.VALUE, BarcodeSettingType.TEXT, Settings.BARCODE_VALUE)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.width"), new BarcodeTableCellValue(BarcodeSettings.WIDTH, BarcodeSettingType.NUMBER, Settings.BARCODE_DEFAULT_WIDTH)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.height"), new BarcodeTableCellValue(BarcodeSettings.HEIGHT, BarcodeSettingType.NUMBER, Settings.BARCODE_DEFAULT_HEIGHT)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.margin"), new BarcodeTableCellValue(BarcodeSettings.MARGIN, BarcodeSettingType.NUMBER, Settings.BARCODE_DEFAULT_MARGIN)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.barcodecolor"), new BarcodeTableCellValue(BarcodeSettings.BARCODECOLOR, BarcodeSettingType.COLOR, Settings.BARCODE_COLOR)));
        this.observableList.add(new BarcodeTableViewDefinition(Assets.getString("fxml.main.label.backgroundcolor"), new BarcodeTableCellValue(BarcodeSettings.BACKGROUNDCOLOR, BarcodeSettingType.COLOR, Settings.BARCODE_BACKGROUND_COLOR)));
    }

    public ObservableList<BarcodeTableViewDefinition> getObservableList() {
        return this.observableList;
    }
}
