package image;

import java.awt.*;

public class ImageBrightnessCalculator
{

    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;


    private static double colorToGrayscale(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return  red*RED_FACTOR + green*GREEN_FACTOR + blue*BLUE_FACTOR;
    }


    public static double calculateImageBrightness(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double grayScaleTotal =0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grayScaleTotal += colorToGrayscale(image.getPixel(x, y));
            }
        }
        double subImageBrightness= grayScaleTotal/ (height*width);
        return subImageBrightness;
    }
}
