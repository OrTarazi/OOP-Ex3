package ascii_art;

import image.*;
import image_char_matching.*;

/**
 * The class responsible for running the algorithm of converting an image to an Ascii Art.
 * the algorithm works in steps:
 * 1. loads an image
 * 2. divides the image to sub-images, and pads with zero if needed.
 * 3. calculates for each sub-image its brightness
 * 4. matches an ascii char for the calculated brightness.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class AsciiArtAlgorithm {
    private Image image;
    private SubImgCharMatcher charMatcher;
    private int resolution;

    //TODO : check if is appropriate to declare enum here
    public enum RoundType {
        UP, DOWN, ABS
    }


    // TODO: constructor doc
    public AsciiArtAlgorithm(Image image, char[] charset, int resolution, RoundType roundType) {
        this.image = image;
        this.charMatcher = new SubImgCharMatcher(charset,roundType);
        this.resolution = resolution;
    }

    // cant change the signature

    /**
     * runs the algorithm from start to finish.
     *
     * @return a char table of the ascii art
     */
    public char[][] run() {
        Image paddedImage = ImagePadding.padImage(this.image);
        Image[][] subImages = ImageDivision.divideToImages(paddedImage, this.resolution);
        char[][] asciiImg = new char[subImages.length][subImages[0].length];

        for (int row = 0; row < subImages.length; row++) {
            for (int col = 0; col < subImages[row].length; col++) {
                float brightness = ImageBrightness.calculateImageBrightness(subImages[row][col]);
                asciiImg[row][col] = this.charMatcher.getCharByImageBrightness(brightness);
            }
        }

        return asciiImg;
    }
}
