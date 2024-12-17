package image_char_matching;

/**
 * simply gets a boolean table and calculates the brightness level describing the character.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class CharBrightnessCalculator {
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
