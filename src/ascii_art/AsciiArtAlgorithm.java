package ascii_art;

import image.Image;
import image.ImageBrightnessCalculator;
import image_char_matching.*;

import java.awt.*;

public class AsciiArtAlgorithm {


    private int resolution_width;
    private int resolution_height;
    private Image[][] image;
    SubImgCharMatcher charMatcher;

    // constructor
    public AsciiArtAlgorithm(int resolution_width, int resolution_height, char[] charset, Image[][] image) {
        this.charMatcher = new SubImgCharMatcher(charset);
        this.resolution_width = resolution_width;
        this.resolution_height = resolution_height;
        this.image = image;
    }

    // cant change the Hotemet

    public char [][] run(){
        char[][] asciiImage = new char[resolution_height][resolution_width];
        for (int y = 0; y < resolution_height; y++) {
            for (int x = 0; x < resolution_width; x++) {
                asciiImage[y][x] =
                        charMatcher.getCharByImageBrightness(
                                ImageBrightnessCalculator.calculateImageBrightness(image[y][x])
                        );
            }
        }
        return asciiImage;
    }

}
