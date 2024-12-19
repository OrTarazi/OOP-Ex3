package image;

import java.awt.*;

/**
 * This class provides methods for calculating the brightness of an image.
 * The brightness is determined by converting each pixel to grayscale and averaging
 * the grayscale values across the entire image.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class ImageBrightness {
    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;
    private static final int MAX_RGB_VALUE = 255;

    /**
     * @param image calculates the grayscale value of each colored pixel, does an average of all grayscale
     *              values and returns the average, which is the equivalent brightness value of the sub-image.
     * @return the calculated brightness value of a sub-image
     */
    public static double calculateImageBrightness(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double grayScaleTotal = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grayScaleTotal += colorToGrayscale(image.getPixel(row, col));
            }
        }

        return grayScaleTotal / (height * width) / MAX_RGB_VALUE;
    }

    /**
     * uses a formula to calculate the gray-scale from an RGB color, and so converts a set of
     * Red-Green-Blue values to a single value of grayscale.
     *
     * @param color an RGB color.
     * @return grayscale value.
     */
    private static double colorToGrayscale(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return red * RED_FACTOR + green * GREEN_FACTOR + blue * BLUE_FACTOR;
    }
}