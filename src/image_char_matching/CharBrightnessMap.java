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

    /**
     * Searches for the key (ASCII char) with the minimal absolute difference between its normalized brightness
     * and the given brightness.
     *
     * @param brightness calculated brightness of a sub-image.
     * @param roundType  the rounding method to determine how to select the closest character ('ABS', 'DOWN', 'UP').
     * @return the best matching ASCII char for the given brightness value.
     */
    public char getCharByNormalizedBrightness(float brightness, RoundType roundType) {
        Map.Entry<Integer, Float>[] closestMatches = this.findClosestMatches(brightness);
        Map.Entry<Integer, Float> closestAbove = closestMatches[0];
        Map.Entry<Integer, Float> closestBelow = closestMatches[1];

        // Handle rounding based on the specified type
        return switch (roundType) {
            case ABS -> this.findClosestMatchBasedOnAbs(closestAbove, closestBelow, brightness);
            case DOWN -> closestBelow != null ? (char) (int) closestBelow.getKey() :
                    (char) (int) closestAbove.getKey(); // Round down to the closest character below
            case UP -> closestAbove != null ? (char) (int) closestAbove.getKey() :
                    (char) (int) closestBelow.getKey();   // Round up to the closest character above
        };
    }

    /**
     * Helper method to find the closest matches above and below the target brightness.
     * This method returns an array containing the closest matches above and below.
     */
    private Map.Entry<Integer, Float>[] findClosestMatches(float brightness) {
        Map.Entry<Integer, Float> closestAbove = null;
        Map.Entry<Integer, Float> closestBelow = null;
        float smallestDifferenceAbove = Float.MAX_VALUE;
        float smallestDifferenceBelow = Float.MAX_VALUE;

        for (Map.Entry<Integer, Float> entry : this.normalizedBrightnessMap.entrySet()) {
            float difference = entry.getValue() - brightness;

            // If the difference is exactly 0, it's an exact match, return it immediately
            if (difference == 0) {
                return new Map.Entry[]{entry, entry};
            } else if (difference > 0 && difference < smallestDifferenceAbove) { // Round up
                smallestDifferenceAbove = difference;
                closestAbove = entry;
            } else if (difference < 0 && -difference < smallestDifferenceBelow) { // Round down
                smallestDifferenceBelow = -difference;
                closestBelow = entry;
            }
        }

        return new Map.Entry[]{closestAbove, closestBelow};
    }

    // Select the best match based on the absolute difference between brightness and the entries.
    private char findClosestMatchBasedOnAbs(Map.Entry<Integer, Float> closestAbove, Map.Entry<Integer,
            Float> closestBelow, float brightness) {
        if (closestAbove != null && closestBelow != null) {
            if (Math.abs(closestAbove.getValue() - brightness) < Math.abs(closestBelow.getValue() - brightness)) {
                return (char) (int) closestAbove.getKey();
            } else {
                return (char) (int) closestBelow.getKey();
            }
        } else if (closestAbove != null) {
            return (char) (int) closestAbove.getKey();
        } else {
            return (char) (int) closestBelow.getKey();
        }
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