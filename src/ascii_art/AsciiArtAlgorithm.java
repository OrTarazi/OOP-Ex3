package ascii_art;

import image.Image;
import image.ImageBrightness;
import image_char_matching.*;

// TODO: Add documentation
public class AsciiArtAlgorithm {
    private int resolution;
    private Image image;
    private SubImgCharMatcher charMatcher;

    // constructor
    public AsciiArtAlgorithm(int resolution, char[] charset, Image image) {
        this.charMatcher = new SubImgCharMatcher(charset);
        this.resolution = resolution;
        this.image = image;
    }

    // cant change the signature
    public char[][] run() {
        char[][] asciiImage = new char[this.resolution][this.resolution];
//        for (int y = 0; y < resolution_height; y++) {
//            for (int x = 0; x < resolution_width; x++) {
//                asciiImage[y][x] =
//                        this.charMatcher.getCharByImageBrightness(ImageBrightness.calculateImageBrightness(this.image));
//            }
//        }
        return asciiImage;
    }

}
