package image_char_matching;

import java.util.Map;
import java.util.TreeMap;
import ascii_art.AsciiArtAlgorithm.RoundType;

/**
 * a data structure that wraps two instanes of TreeMap from java.Collections.
 * the CharBrightnessMap offers the functionality of a map, but also manages the normalization of the
 * values, which makes it more complex, yet effetive.
 */
public class CharBrightnessMap {
    private static final Float MAP_MISS_VALUE = -1.0f;
    private static final float MIN_POSSIBLE_BRIGHTNESS = 0;
    private static final float MAX_POSSIBLE_BRIGHTNESS = 1;
    // Stores raw brightness values
    private final TreeMap<Integer, Float> rawBrightnessMap;
    // Stores normalized brightness values

    private float minBrightness = MAX_POSSIBLE_BRIGHTNESS;
    private final TreeMap<Integer, Float> normalizedBrightnessMap;
    private float maxBrightness = MIN_POSSIBLE_BRIGHTNESS;

    /**
     * constructor for the data-structure. initializes two TreeMap when an instance is created.
     */
    public CharBrightnessMap() {
        this.rawBrightnessMap = new TreeMap<>();
        this.normalizedBrightnessMap = new TreeMap<>();
    }

    /**
     * adds char to the ascii char set.
     *
     * @param c char to be added to ascii set
     */
    public void addChar(char c) {
        float brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        this.rawBrightnessMap.put((int) c, brightness); // Always store raw brightness


        if (brightness > this.maxBrightness || brightness < this.minBrightness){
            this.adjustMinAndMaxBrightness(brightness);
        }
        if (this.normalizedBrightnessMap.size()>1){
            this.normalizedBrightnessMap.put((int) c, this.normalizeBrightness(brightness));
            normalizeAllValues();
        }
        else {
           this.normalizedBrightnessMap.put((int) c, brightness);
        }
        System.out.println("f");

    }

    // TODO: handle exceptions

    /**
     * searches for the key (Ascii char) with the minimal absolute difference between its value
     * (Brightness) and the given brightness
     *
     * @param brightness calculated brightness of a sub-image.
     * @return best matching ascii char for the given brightness value, making it the best fit to represent
     * the sub-image in the final ascii art.
     */
    public char getCharByNormalizedBrightness(float brightness, RoundType roundType) {
        Map.Entry<Integer, Float> closestAbove = null;
        Map.Entry<Integer, Float> closestBelow = null;

        for (Map.Entry<Integer, Float> entry : this.normalizedBrightnessMap.entrySet()) {
            if (entry.getValue() == MAX_POSSIBLE_BRIGHTNESS){
                closestAbove = entry;
            }
            if (entry.getValue() == MIN_POSSIBLE_BRIGHTNESS){
                closestBelow = entry;
            }
        }

        float smallestDifferenceAbove = MAX_POSSIBLE_BRIGHTNESS;
        float smallestDifferenceBelow = -MAX_POSSIBLE_BRIGHTNESS;

        for (Map.Entry<Integer, Float> entry : this.normalizedBrightnessMap.entrySet()) {


            float difference = entry.getValue() - brightness;
            if (difference > 0) {
                if (difference < smallestDifferenceAbove) {
                    smallestDifferenceAbove = difference;
                    closestAbove = entry;
                }
            }
            if (difference < 0) {
                if (difference > smallestDifferenceBelow) {
                    smallestDifferenceBelow = difference;
                    closestBelow = entry;
                }
            }
        }

        if (closestAbove == null || closestBelow == null) {
            throw new IllegalStateException("No characters in brightness map.");
        }

        return switch (roundType) {
            case ABS -> (char) Math.max(Math.abs(closestAbove.getKey()), Math.abs(closestBelow.getKey()));
            case DOWN -> (char) (int) closestBelow.getKey();
            case UP -> (char) (int) closestAbove.getKey();
        };
    }

    /**
     * gets a char, and deletes the entry with the char as aa key, from both normalized and raw maps.
     *
     * @param c key in map to be removed
     */
    public void removeChar(char c) {
        float brightness = this.rawBrightnessMap.remove((int) c);
        this.normalizedBrightnessMap.remove((int) c);

        if (brightness == this.maxBrightness || brightness == this.minBrightness) {
            this.recalculateMinMaxBrightness();
            if (this.rawBrightnessMap.size() > 1) {
                this.normalizeAllValues();
            }
        }

    }

    /**
     * after removing an ascii char from the set, it is needed to re-evaluate the minimum or maximum
     * brightness values, as the removed ascii char could be the most or least bright character in the set
     * before removal.
     */
    private void recalculateMinMaxBrightness() {

        this.minBrightness = MAX_POSSIBLE_BRIGHTNESS;
        this.maxBrightness = MIN_POSSIBLE_BRIGHTNESS;

        for (float brightness : this.rawBrightnessMap.values()) {
            this.adjustMinAndMaxBrightness(brightness);
        }
    }

    /**
     * does a complete normalization operation on normalized map.
     * it's not needed to be called when minimum/brightness haven't changed after adding an ascii char.
     */
    private void normalizeAllValues() {

        for (Map.Entry<Integer, Float> entry : this.rawBrightnessMap.entrySet()) {
            float normalized = this.normalizeBrightness(entry.getValue());

            this.normalizedBrightnessMap.put(entry.getKey(), normalized);
        }
    }

    /**
     * does linear stretch over the current set of ascii chars and their brightness values.
     * re-evaluates the normalizedMap values per key.
     * the method is called smartly, only when adding an entry to the map where the brightness value
     * outreaches the current minimum and maximum.
     *
     * @param addedBrightness the new brightness value in the map that changes the min/max values.
     * @return normalized brightness
     */
    private float normalizeBrightness(float addedBrightness) {
        // TODO: Assumption, condition can br removed (check)
        // Avoid division by zero when all values are identical
        if (this.minBrightness == this.maxBrightness) {
            return addedBrightness;
        }
        return (addedBrightness - this.minBrightness) / (this.maxBrightness - this.minBrightness);
    }

    /**
     * re-evaluates the maximum and minimum brightness in the class' ascii-char set.
     * the method is called smartly, only when adding an entry to the map where the brightness value
     * outreaches the current minimum and maximum.
     *
     * @param addedBrightness the new brightness value in the map that changes the min/max values.
     */
    private void adjustMinAndMaxBrightness(float addedBrightness) {
        if (addedBrightness < this.minBrightness) {
            this.minBrightness = addedBrightness;
        }

        if (addedBrightness > this.maxBrightness) {
            this.maxBrightness = addedBrightness;
        }
    }
}