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
    private BrightnessMemento brightnessMemento;
    private Image paddedImage;
    private Image[][] subImages;

    /**
     * Constructs a new `AsciiArtAlgorithm` instance.
     * Initializes the algorithm with the provided image, character matcher, and resolution.
     * These inputs define the image to be processed, the set of ASCII characters used for brightness mapping,
     * and the level of detail in the resulting ASCII art.
     *
     * @param image      the input image to convert to ASCII art.
     * @param charMatcher the character matcher responsible for mapping brightness values to ASCII characters.
     * @param resolution  the resolution of the ASCII art, representing the number of image pixels
     *                    mapped to a single ASCII character in both dimensions.
     */
    public AsciiArtAlgorithm(Image image, BrightnessMemento memento, SubImgCharMatcher charMatcher, int resolution) {
        this.image = image;
        this.charMatcher = charMatcher;
        this.resolution = resolution;
        this.brightnessMemento = memento;
        this.paddedImage = ImagePadding.padImage(this.image);
        this.subImages = ImageDivision.divideToImages(paddedImage, this.resolution);

    }

    /**
     * runs the algorithm from start to finish.
     *
     * @return a char table of the ascii art
     */
    public char[][] run() {

        char[][] asciiImg = new char[subImages.length][subImages[0].length];
        float [][] newBrightnessMap = new float[subImages.length][subImages[0].length];
        for (int row = 0; row < subImages.length; row++) {
            for (int col = 0; col < subImages[row].length; col++) {
                float brightness;

                // if the algorithm can avoid unnecessary re-calculation of sub-image brightnesses:
                if (brightnessMemento.isLastStateValid() && brightnessMemento.restoreState() != null) {
                    brightness = brightnessMemento.restoreState()[row][col];
                }

                // if the resolution has changed and the algorithm has to re-calculate:
                else {
                    brightness = ImageBrightness.calculateImageBrightness(subImages[row][col]);
                    newBrightnessMap[row][col] = brightness;
                }

                // either way, get the char for the given brightness
                asciiImg[row][col] = this.charMatcher.getCharByImageBrightness(brightness);
            }
        }

        // if sub-image brightness calculations were actually made, save them and set them as 'valid' for next run
        if (!(brightnessMemento.isLastStateValid() && brightnessMemento.restoreState() != null)){
            this.brightnessMemento.saveState(newBrightnessMap);
            this.brightnessMemento.setLastStateValidity(true);
        }
        return asciiImg;
    }
}
