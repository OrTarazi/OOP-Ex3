package ascii_art;

import image.*;
import image_char_matching.*;

// TODO: change documentation because class has benn changed

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
    private final SubImgCharMatcher charMatcher;
    private final BrightnessMemento brightnessMemento;
    private final Image[][] subImages;

    // TODO: change documentation because function has benn changed

    /**
     * Constructs a new `AsciiArtAlgorithm` instance.
     * Initializes the algorithm with the provided image, character matcher, and resolution.
     * These inputs define the image to be processed, the set of ASCII characters used for brightness mapping,
     * and the level of detail in the resulting ASCII art.
     *
     * @param image       the input image to convert to ASCII art.
     * @param charMatcher the character matcher responsible for mapping brightness values to ASCII characters.
     * @param resolution  the resolution of the ASCII art, representing the number of image pixels
     *                    mapped to a single ASCII character in both dimensions.
     */
    public AsciiArtAlgorithm(Image image,
                             BrightnessMemento memento, // TODO: check warning
                             SubImgCharMatcher charMatcher,
                             int resolution) {
        this.charMatcher = charMatcher;
        this.brightnessMemento = memento;
        Image paddedImage = ImagePadding.padImage(image);
        this.subImages = ImageDivision.divideToImages(paddedImage, resolution);
    }

    // TODO: change documentation because function has benn changed

    /**
     * runs the algorithm from start to finish.
     *
     * @return a char table of the ascii art
     */
    public char[][] run() {
        char[][] asciiImg = new char[this.subImages.length][this.subImages[0].length];
        double[][] newBrightnessMap = new double[this.subImages.length][this.subImages[0].length];

        for (int row = 0; row < this.subImages.length; row++) {
            for (int col = 0; col < this.subImages[row].length; col++) {
                double brightness;

                // if the algorithm can avoid unnecessary re-calculation of sub-image brightnesses:
                if (this.brightnessMemento.isLastStateValid() &&
                        this.brightnessMemento.restoreState() != null) {
                    brightness = this.brightnessMemento.restoreState()[row][col];
                } else { // if the resolution has changed and the algorithm has to re-calculate:
                    brightness = ImageBrightness.calculateImageBrightness(this.subImages[row][col]);
                    newBrightnessMap[row][col] = brightness;
                }

                // either way, get the char for the given brightness
                asciiImg[row][col] = this.charMatcher.getCharByImageBrightness(brightness);
            }
        }

        // if sub-image brightness re-calculations were made, save them and set them as 'valid' for next run
        if (!(this.brightnessMemento.isLastStateValid() && this.brightnessMemento.restoreState() != null)) {
            this.brightnessMemento.saveState(newBrightnessMap);
            this.brightnessMemento.setLastStateValidity(true);
        }

        return asciiImg;
    }
}
