package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

import ascii_art.RoundType;

/**
 * a data structure that wraps two instances of TreeMap from java.Collections.
 * the CharBrightnessMap offers the functionality of a map, but also manages the normalization of the
 * values, which makes it more complex, yet effective.
 */
// TODO: FIX DOCUMENTATION
public class CharBrightnessMap {
    private static final float MIN_POSSIBLE_BRIGHTNESS = 0;
    private static final float MAX_POSSIBLE_BRIGHTNESS = 1;

    // Stores raw brightness values
    private final TreeMap<Integer, Float> rawBrightnessMap;
    // Stores normalized brightness values
    private final TreeMap<Integer, Float> normalizedBrightnessMap;
    private float minBrightness;
    private float maxBrightness;

    /**
     * constructor for the data-structure. initializes two TreeMap when an instance is created.
     */
    public CharBrightnessMap() {
        this.rawBrightnessMap = new TreeMap<>();
        this.normalizedBrightnessMap = new TreeMap<>();
        // Init default values to make first conditions be true
        this.minBrightness = MAX_POSSIBLE_BRIGHTNESS;
        this.maxBrightness = MIN_POSSIBLE_BRIGHTNESS;
    }

    /**
     * adds char to the ascii char set.
     *
     * @param c char to be added to ascii set
     */
    public void addChar(char c) {
        float brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        this.rawBrightnessMap.put((int) c, brightness); // Always store raw brightness

        if (brightness > this.maxBrightness || brightness < this.minBrightness) {
            this.adjustMinAndMaxBrightness(brightness);
        }
        if (this.normalizedBrightnessMap.size() > 1) {
            this.normalizedBrightnessMap.put((int) c, this.normalizeBrightness(brightness));
            this.normalizeAllValues();
        } else {
            this.normalizedBrightnessMap.put((int) c, brightness);
        }

    }

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
     * After removing an ascii char from the set, it is needed to re-evaluate the minimum or maximum
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
     * outreaches the current minimum and maximum (we can assume they are different
     *
     * @param addedBrightness the new brightness value in the map that changes the min/max values.
     * @return normalized brightness
     */
    private float normalizeBrightness(float addedBrightness) {
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

    /**
     * Returns the number of ASCII characters currently in the brightness map.
     *
     * @return the size of the map, representing the total number of characters in the set.
     */
    public int getSize() {
        return this.rawBrightnessMap.size();
    }

    /**
     * Prints all the ASCII characters currently in the brightness map to the console.
     * Characters are printed in a single line, separated by spaces, in the order they
     * appear in the internal TreeMap.
     */
    public void printChars() {
        int count = 0;
        for (int key : this.rawBrightnessMap.keySet()) {
            System.out.print((char) key);
            if (++count < this.rawBrightnessMap.size()) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Checks if a specific ASCII character is present in the brightness map.
     *
     * @param c the character to check.
     * @return true if the character exists in the map, false otherwise.
     */
    public boolean containsChar(char c) {
        return this.rawBrightnessMap.containsKey((int) c);
    }
}