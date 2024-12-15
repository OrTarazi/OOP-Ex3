package image_char_matching;

/**
 * a facade for using the CharBrightnessMap and so simplifies handling removal and addition of ascii chars
 * to the set.
 */
public class SubImgCharMatcher {

    private CharBrightnessMap brightnessMap;

    /**
     * constructor for the matcher
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
     *
     * searches for the key (Ascii char) with the minimal absolute difference between its value
     * (Brightness) and the given brightness.
     * @param brightness calculated brightness of a sub-image.
     * @return best matching ascii char for the given brightness value, making it the best fit to represent
     * the sub-image in the final ascii art.
     */
    public char getCharByImageBrightness(float brightness) {
        return this.brightnessMap.getCharByNormalizedBrightness(brightness);
    }

    /**
     * adds char to the ascii char set. CharBrightnessMap class handles the adding operation.
     * @param c char to be added to ascii set
     */
    public void addChar(char c) {
        this.brightnessMap.addChar(c);
    }

    /**
     * removes char from the ascii char set. CharBrightnessMap class handles the removal operation
     * @param c char to be removed
     */
    public void removeChar(char c) {
        this.brightnessMap.removeChar(c);
    }
}