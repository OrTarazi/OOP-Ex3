package ascii_art;

import image.Image;
import image_char_matching.*;

import java.awt.*;

public class AsciiArtAlgorithm {

    private static final double RED_FACTOR = 0.2126;
    private static final double GREEN_FACTOR = 0.7152;
    private static final double BLUE_FACTOR = 0.0722;

    private int resolution_width;
    private int resolution_height;
    private Image image;
    SubImgCharMatcher charMatcher;

    // constructor
    public AsciiArtAlgorithm(int resolution_width, int resolution_height, char[] charset, Image image) {
        this.charMatcher = new SubImgCharMatcher(charset);
        this.resolution_width = resolution_width;
        this.resolution_height = resolution_height;
        this.image = image;
    }

    private static double colorToGrayscale(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return  red*RED_FACTOR + green*GREEN_FACTOR + blue*BLUE_FACTOR;
    }



    private double calculateSubImageBrightness(Image image) {
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

    // cant change the Hotemet
    public char [][] run(){
        char[][] asciiImage = new char[resolution_height][resolution_width];
        for (int y = 0; y < resolution_height; y++) {
            for (int x = 0; x < resolution_width; x++) {
                asciiImage[resolution_height][resolution_width] = charMatcher.getCharByImageBrightness(calculateSubImageBrightness(image));
            }
        }
        return asciiImage;
    }

}
