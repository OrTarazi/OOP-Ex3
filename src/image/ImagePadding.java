package image;

import java.awt.*;

/**
 * The ImagePadding class provides functionality to pad an image to the nearest power of two.
 * It calculates the required padding size for both dimensions (width and height) and then
 * applies the padding, filling the padded areas with a white color.
 * This class helps in preparing images for processing that require dimensions that are powers of two,
 * such as some image processing algorithms or graphical applications that benefit from power-of-two sizes.
 *
 * @author: Agam Hershko and Or Tarazi
 */
public class ImagePadding {
    private static final int POWER_BASE = 2;

    /**
     * Pads the image to the next power of two in both dimensions.
     *
     * @param image The image to pad.
     * @return A new padded image.
     */
    public static Image padImage(Image image) {
        int paddedImageHeight = getNextPower(image.getHeight());
        int paddedImageWidth = getNextPower(image.getWidth());

        int paddingSizeHeight = getPaddingSize(image.getHeight(), paddedImageHeight);
        int paddingSizeWidth = getPaddingSize(image.getWidth(), paddedImageWidth);

        Color[][] paddedPixelMatrix = new Color[paddedImageHeight][paddedImageWidth];

        // Initialize the padded pixel matrix with padding color (white)
        initPaddedPixelMatrix(paddedPixelMatrix, image, paddingSizeHeight, paddingSizeWidth);

        // Create and return the padded image
        return new Image(paddedPixelMatrix, paddedImageWidth, paddedImageHeight);
    }

    /**
     * Finds the next power of two greater than or equal to the given number.
     *
     * @param number The number to find the next power of two for.
     * @return The next power of two greater than or equal to the given number.
     */
    private static int getNextPower(int number) {
        return (int) Math.pow(POWER_BASE, Math.ceil(Math.log(number) / Math.log(POWER_BASE)));
    }

    /**
     * Calculates the padding size required to reach the next power of two.
     *
     * @param imageSize       The original size of the image.
     * @param paddedImageSize The new padded size of the image.
     * @return The amount of padding required.
     */
    private static int getPaddingSize(int imageSize, int paddedImageSize) {
        return (paddedImageSize - imageSize) / 2;
    }

    /**
     * Initializes the padded pixel matrix by copying the original image pixels and applying padding.
     *
     * @param paddedPixelMatrix The matrix to store the padded image pixels.
     * @param image             The original image.
     * @param paddingSizeHeight The amount of vertical padding.
     * @param paddingSizeWidth  The amount of horizontal padding.
     */
    private static void initPaddedPixelMatrix(Color[][] paddedPixelMatrix, Image image,
                                              int paddingSizeHeight, int paddingSizeWidth) {
        // Copy original image pixels into the center of the padded matrix
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                paddedPixelMatrix[paddingSizeHeight + row][paddingSizeWidth + col] = image.getPixel(row, col);
            }
        }

        // Apply padding (white) around the original image
        applyPadding(paddedPixelMatrix, image, paddingSizeHeight, paddingSizeWidth);
    }

    /**
     * Applies padding around the image in the padded pixel matrix.
     *
     * @param paddedPixelMatrix The matrix to store the padded image pixels.
     * @param image             The original image.
     * @param paddingSizeHeight The amount of vertical padding.
     * @param paddingSizeWidth  The amount of horizontal padding.
     */
    private static void applyPadding(Color[][] paddedPixelMatrix, Image image,
                                     int paddingSizeHeight, int paddingSizeWidth) {
        // Apply padding to the top and bottom sides
        for (int row = 0; row < paddedPixelMatrix.length; row++) {
            for (int col = 0; col < paddedPixelMatrix[0].length; col++) {
                if (row < paddingSizeHeight || row >= image.getHeight() + paddingSizeHeight ||
                        col < paddingSizeWidth || col >= image.getWidth() + paddingSizeWidth) {
                    paddedPixelMatrix[row][col] = Color.WHITE;  // White padding color
                }
            }
        }
    }
}