package image_char_matching;

import ascii_art.RoundType;

/**
 * a facade for using the CharBrightnessMap and so simplifies handling removal and addition of ascii chars
 * to the set.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class SubImgCharMatcher {
    private final CharBrightnessMap brightnessMap;
    private RoundType roundType;

    /**
     * constructor for the matcher
     *
     * @param charset the initial set of ascii characters to be used in the ascii art.
     */
    public SubImgCharMatcher(char[] charset) {
        this.brightnessMap = new CharBrightnessMap();

        for (char c : charset) {
            this.addChar(c);
        }
    }

    /**
     * **** IMPLEMENTED IN CharBrightnessMap CLASS ****
     * <p>
     * searches for the key (Ascii char) with the minimal absolute difference between its value
     * (Brightness) and the given brightness.
     *
     * @param brightness calculated brightness of a sub-image.
     * @return best matching ascii char for the given brightness value, making it the best fit to represent
     * the sub-image in the final ascii art.
     */
    public char getCharByImageBrightness(double brightness) {
        return this.brightnessMap.getCharByNormalizedBrightness(brightness, this.roundType);
    }

    /**
     * adds char to the ascii char set. CharBrightnessMap class handles the adding operation.
     *
     * @param c char to be added to ascii set
     */
    public void addChar(char c) {
        this.brightnessMap.addChar(c);
    }

    /**
     * removes char from the ascii char set. CharBrightnessMap class handles the removal operation
     *
     * @param c char to be removed
     */
    public void removeChar(char c) {
        this.brightnessMap.removeChar(c);
    }

    /**
     * Returns the number of characters currently in the ASCII character set.
     *
     * @return the size of the character set managed by this matcher.
     */
    public int getCharsNumber() {
        return this.brightnessMap.getSize();
    }

    /**
     * Prints all the characters in the ASCII character set by order
     */
    public void printChars() {
        this.brightnessMap.printChars();
    }

    /**
     * Checks if a specific character exists in the ASCII character set.
     *
     * @param c the character to check.
     * @return true if the character is present in the set, false otherwise.
     */
    public boolean containsChar(char c) {
        return this.brightnessMap.containsChar(c);
    }

    /**
     * Sets the rounding type for the current object.
     *
     * @param roundType The rounding type to be set.
     */
    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }
}