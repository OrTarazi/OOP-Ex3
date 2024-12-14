package image;

import java.awt.*;

public class ImageBrightness {
    private static final float RED_FACTOR = 0.2126f;
    private static final float GREEN_FACTOR = 0.7152f;
    private static final float BLUE_FACTOR = 0.0722f;

    public static float calculateImageBrightness(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float grayScaleTotal = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grayScaleTotal += colorToGrayscale(image.getPixel(col, row));
            }
        }

        return grayScaleTotal / (height * width);
    }

    private static float colorToGrayscale(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return red * RED_FACTOR + green * GREEN_FACTOR + blue * BLUE_FACTOR;
    }
}