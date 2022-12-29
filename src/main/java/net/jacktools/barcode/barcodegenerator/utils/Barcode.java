package net.jacktools.barcode.barcodegenerator.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

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
     * @param barcodeText The barcode text
     * @param width       thw image width
     * @param height      thhe image height
     * @return the barcode image
     * @throws Exception if an error occours
     */
    public static BufferedImage generateEAN13BarcodeImage(String barcodeText, int width, int height) throws Exception {
        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, width, height);
        LAST_BARCODE_IMAGE = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return LAST_BARCODE_IMAGE;
    }

    /**
     * Writes the last generated barcode image to a file.
     *
     * @param filePath the file path
     * @throws IOException if an error occours
     */
    public static void barcodeToFile(Path filePath) throws IOException {
        ImageIO.write(LAST_BARCODE_IMAGE, "png", filePath.toFile());
    }
}
