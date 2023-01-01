package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Barcode {

    public static BufferedImage LAST_BARCODE_IMAGE;

    /**
     * Converts a BufferedImage to an javaFX image.
     *
     * @param bufferedImage the bufferedImage
     * @return the javaFX image
     */
    public static Image convertToFxImage(BufferedImage bufferedImage) {
        if (bufferedImage != null) {
            WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
            return new ImageView(writableImage).getImage();
        } else {
            return null;
        }
    }

    /**
     * Creates an EAN138 barcode image
     *
     * @param value  The barcode text
     * @param format the barcode format
     * @param width  thw image width
     * @param height the image height
     * @return the barcode image
     */
    public static BufferedImage create(String value, BarcodeFormat format, int width, int height) throws WriterException {
        Writer writer;
        switch (format) {
            case EAN_8:
                writer = new EAN8Writer();
                break;
            case EAN_13:
                writer = new EAN13Writer();
                break;
            case UPC_A:
                writer = new UPCAWriter();
                break;
            case QR_CODE:
                writer = new QRCodeWriter();
                break;
            case CODE_39:
                writer = new Code39Writer();
                break;
            case CODE_128:
                writer = new Code128Writer();
                break;
            case ITF:
                writer = new ITFWriter();
                break;
            case PDF_417:
                writer = new PDF417Writer();
                break;
            case CODABAR:
                writer = new CodaBarWriter();
                break;
            case DATA_MATRIX:
                writer = new DataMatrixWriter();
                break;
            case AZTEC:
                writer = new AztecWriter();
                break;
            default:
                throw new IllegalArgumentException(Assets.getString("barcode.no.formatter", format.toString()));
        }
        BitMatrix bitMatrix = writer.encode(value, format, width, height);
        LAST_BARCODE_IMAGE = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return LAST_BARCODE_IMAGE;
    }

    /**
     * Writes the last generated barcode image to a file.
     *
     * @param stage the parent stage
     * @throws IOException if an error occurs
     */
    public static void toFile(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg"));
        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            ImageIO.write(LAST_BARCODE_IMAGE, file.getName().substring(file.getName().lastIndexOf(".") + 1), file);
        }
    }

    /**
     * Copy the barcode image to the clipboard.
     *
     * @param image the image.
     */
    public static void toClipboard(Image image) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }
}
