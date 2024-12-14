package ascii_art;

import image.*;
import image_char_matching.*;

// TODO: Add documentation
public class AsciiArtAlgorithm {
    private Image image;
    private SubImgCharMatcher charMatcher;
    private int resolution;

    // constructor
    public AsciiArtAlgorithm(Image image, char[] charset, int resolution) {
        this.image = image;
        this.charMatcher = new SubImgCharMatcher(charset);
        this.resolution = resolution;
    }

    // cant change the signature
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
