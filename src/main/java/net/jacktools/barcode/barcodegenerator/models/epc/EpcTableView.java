/*
 * Copyright (c) 2023.
 */

package net.jacktools.barcode.barcodegenerator.models.epc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.jacktools.barcode.barcodegenerator.utils.Assets;
import net.jacktools.barcode.barcodegenerator.utils.Settings;

public class EpcTableView {
    final ObservableList<EpcTableViewDefinition> observableList = FXCollections.observableArrayList();

    public EpcTableView() {
        super();
        this.observableList.add(new EpcTableViewDefinition(Assets.getString("fxml.main.label.width"), new EpcTableCellValue(EpcSettings.WIDTH, EpcSettingType.NUMBER, Settings.QRCODE_DEFAULT_WIDTH)));
        this.observableList.add(new EpcTableViewDefinition(Assets.getString("fxml.main.label.height"), new EpcTableCellValue(EpcSettings.HEIGHT, EpcSettingType.NUMBER, Settings.QRCODE_DEFAULT_HEIGHT)));
        this.observableList.add(new EpcTableViewDefinition(Assets.getString("fxml.main.label.margin"), new EpcTableCellValue(EpcSettings.MARGIN, EpcSettingType.NUMBER, Settings.QRCODE_DEFAULT_MARGIN)));
        this.observableList.add(new EpcTableViewDefinition(Assets.getString("fxml.main.label.barcodecolor"), new EpcTableCellValue(EpcSettings.QRCODECOLOR, EpcSettingType.COLOR, Settings.QRCODE_COLOR)));
        this.observableList.add(new EpcTableViewDefinition(Assets.getString("fxml.main.label.backgroundcolor"), new EpcTableCellValue(EpcSettings.BACKGROUNDCOLOR, EpcSettingType.COLOR, Settings.QRCODE_BACKGROUND_COLOR)));
    }

    public ObservableList<EpcTableViewDefinition> getObservableList() {
        return this.observableList;
    }
}
