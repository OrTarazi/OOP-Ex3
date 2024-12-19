package image_char_matching;

/**
 * Utility class for calculating the brightness level of a character.
 *
 * <p>The brightness is determined based on a boolean table representation of the character,
 * where each `true` value represents a white pixel. The brightness is calculated as the ratio
 * of white pixels to the total number of pixels.</p>
 *
 * @author Or Tarazi, Agam Hershko
 */
public class CharBrightnessCalculator {
    /**
     * Calculates the brightness level of a given character.
     *
     * <p>The method converts the character into a boolean table representation,
     * where `true` values represent white pixels and `false` values represent black pixels.
     * The brightness is computed as the ratio of white pixels to the total number of pixels.</p>
     *
     * @param c the character for which brightness is to be calculated.
     * @return the brightness level as a float between 0 (fully black) and 1 (fully white).
     */
    public static float calculateCharBrightness(char c) {
        boolean[][] charBinaryImage = CharConverter.convertToBoolArray(c);
        int totalPixels = charBinaryImage.length * charBinaryImage[0].length;
        int whitePixels = 0;

        for (boolean[] row : charBinaryImage) {
            for (boolean isPixelWhite : row) {
                if (isPixelWhite) {
                    whitePixels++;
                }
            }
        }

        return whitePixels / (float) totalPixels;
    }
}
