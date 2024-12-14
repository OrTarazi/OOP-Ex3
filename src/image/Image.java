package image;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents an image that is stored as a 2D array of {java.awt.Color} objects.
 * Provides functionality to load an image from a file, access its pixels,
 * and save it back to a file.
 *
 * @author Agam Hershko and Or Tarazi
 */
public class Image {
    private final Color[][] pixelArray;
    private final int width;
    private final int height;

    /**
     * Constructs an Image by reading a file.
     *
     * @param filename the path to the image file.
     * @throws IOException if the file cannot be read.
     */
    public Image(String filename) throws IOException {
        BufferedImage im = ImageIO.read(new File(filename));
        this.width = im.getWidth();
        this.height = im.getHeight();


        this.pixelArray = new Color[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                this.pixelArray[i][j] = new Color(im.getRGB(j, i));
            }
        }
    }

    /**
     * Constructs an Image using a 2D array of Color objects.
     *
     * @param pixelArray a 2D array representing the image's pixels.
     * @param width      the width of the image.
     * @param height     the height of the image.
     *                                                       TODO: Check validity
     */
    public Image(Color[][] pixelArray, int width, int height) {
        this.pixelArray = pixelArray;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width of the image.
     *
     * @return the width of the image, in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the image.
     *
     * @return the height of the image, in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the color of a specific pixel.
     *
     * @param x the row index of the pixel (0-based).
     * @param y the column index of the pixel (0-based).
     * @return the Color object of the specified pixel.
     * @throws ArrayIndexOutOfBoundsException if the indices are out of bounds.
     *                                               TODO: Throws and catch exception
     */
    public Color getPixel(int x, int y) {
        return this.pixelArray[x][y];
    }

    /**
     * Saves the image to a file in JPEG format.
     *
     * @param fileName the name of the output file (without extension).
     * @throws RuntimeException if an error occurs while writing the file.
     *                                        TODO: Throws and catch exception
     */
    public void saveImage(String fileName) {
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(pixelArray[0].length, pixelArray.length,
                BufferedImage.TYPE_INT_RGB);
        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < pixelArray.length; x++) {
            for (int y = 0; y < pixelArray[x].length; y++) {
                bufferedImage.setRGB(y, x, pixelArray[x][y].getRGB());
            }
        }
        File outputfile = new File(fileName + ".jpeg");
        try {
            ImageIO.write(bufferedImage, "jpeg", outputfile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
