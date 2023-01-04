package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Barcode {

    /**
     * Creates an EAN138 barcode image
     *
     * @param value  The barcode text
     * @param format the barcode format
     * @param width  thw image width
     * @param height the image height
     * @return the barcode image as buffered image
     */
    public static BufferedImage create(String value, SupportedBarcodeFormat format, int width, int height) throws WriterException {
        Writer writer;
        switch (format) {
            case AZTEC:
                writer = new AztecWriter();
                break;
            case CODE_39:
                writer = new Code39Writer();
                break;
            case CODE_93:
                writer = new Code93Writer();
                break;
            case CODE_128:
                writer = new Code128Writer();
                break;
            case CODABAR:
                writer = new CodaBarWriter();
                break;
            case DATA_MATRIX:
                writer = new DataMatrixWriter();
                break;
            case EAN_8:
                writer = new EAN8Writer();
                break;
            case EAN_13:
                writer = new EAN13Writer();
                break;
            case ITF:
                writer = new ITFWriter();
                break;
            case PDF_417:
                writer = new PDF417Writer();
                break;
            case QR_CODE:
                writer = new QRCodeWriter();
                break;
            case UPC_A:
                writer = new UPCAWriter();
                break;
            case UPC_E:
                writer = new UPCEWriter();
                break;
            default:
                throw new IllegalArgumentException(Assets.getString("barcode.no.formatter", format.toString()));
        }
        BitMatrix bitMatrix = writer.encode(value, format.getBarcodeFormat(), width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Creates an EAN138 barcode image
     *
     * @param value           The barcode text
     * @param format          the barcode format
     * @param width           thw image width
     * @param height          the image height
     * @param backgroundColor the background color
     * @param barcodeColor    the barcode color
     * @return the barcode image as byte array
     */
    public static byte[] createByteArray(String value, SupportedBarcodeFormat format, int width, int height, Color backgroundColor, Color barcodeColor) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = create(value, format, width, height);
        Image image = bufferedImageToFxImage(bufferedImage, backgroundColor, barcodeColor);
        // Perhaps we do in the future some other thins with the image here.
        ImageIO.write(fxImageToBufferedImage(image), "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Converts a BufferedImage to an javaFX image.
     *
     * @param bufferedImage   the bufferedImage
     * @param backgroundColor the background color
     * @param barcodeColor    the barcode color
     * @return the javaFX image
     */
    public static Image bufferedImageToFxImage(BufferedImage bufferedImage, Color backgroundColor, Color barcodeColor) {
        if (bufferedImage != null) {
            WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    if (bufferedImage.getRGB(x, y) == 0xFF000000) {
                        bufferedImage.setRGB(x, y, barcodeColor.hashCode());
                        pixelWriter.setColor(x, y, barcodeColor);
                    } else {
                        bufferedImage.setRGB(x, y, backgroundColor.hashCode());
                        pixelWriter.setColor(x, y, backgroundColor);
                    }
                }
            }
            return new ImageView(writableImage).getImage();
        } else {
            return null;
        }
    }

    /**
     * Convert a fx image to a buffered image.
     *
     * @param image the fx image
     * @return the buffered image
     */
    public static BufferedImage fxImageToBufferedImage(Image image) {
        if (image != null) {
            int width = Double.valueOf(image.getWidth()).intValue();
            int height = Double.valueOf(image.getHeight()).intValue();
            BufferedImage bufferedImage = new BufferedImage(width, height, 1);
            PixelReader pixelReader = image.getPixelReader();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
                }
            }
            return bufferedImage;
        } else {
            return null;
        }
    }

    /**
     * Writes the last generated barcode image to a file.
     *
     * @param stage the parent stage
     * @param image the image to save.
     * @throws IOException if an error occurs
     */
    public static void toFile(Stage stage, Image image) throws IOException {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg"));
        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            ImageIO.write(fxImageToBufferedImage(image), file.getName().substring(file.getName().lastIndexOf(".") + 1), file);
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
