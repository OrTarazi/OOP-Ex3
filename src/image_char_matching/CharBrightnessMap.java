package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

import ascii_art.RoundType;

/**
 * A data structure that wraps two instances of TreeMap from the Java Collections framework.
 * The `CharBrightnessMap` maps ASCII characters to brightness values and normalizes them
 * to allow efficient brightness-based character matching for ASCII art generation.
 * <p>
 * Normalization ensures brightness values are linearly scaled between 0 and 1 for consistency
 * and accurate comparisons.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class CharBrightnessMap {
    private static final double MIN_POSSIBLE_BRIGHTNESS = 0;
    private static final double MAX_POSSIBLE_BRIGHTNESS = 1;

    // Stores raw brightness values
    private final TreeMap<Integer, Double> rawBrightnessMap;
    // Stores normalized brightness values
    private final TreeMap<Integer, Double> normalizedBrightnessMap;
    private double minBrightness;
    private double maxBrightness;

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
        double brightness = CharBrightnessCalculator.calculateCharBrightness(c);
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
     * Finds the ASCII character that best matches the given normalized brightness value.
     * This method searches the brightness map for the closest brightness values above
     * and below the provided brightness. It then selects the best match based on the specified rounding type.
     *
     * @param brightness the normalized brightness value (between 0 and 1) to match.
     * @param roundType  the rounding strategy to use when selecting the closest character:
     *                   - ABS: Select the character with the smallest absolute difference.
     *                   - UP: Select the closest character with brightness above or equal to given value.
     *                   - DOWN: Select the closest character with brightness below or equal to given value.
     * @return the ASCII character that best matches the brightness value based on the rounding type.
     */
    public char getCharByNormalizedBrightness(double brightness, RoundType roundType) {
        Map.Entry<Integer, Double> closestAbove = null;
        Map.Entry<Integer, Double> closestBelow = null;

        // Initialize with double.MAX_VALUE to ensure any valid difference is smaller
        double smallestDifferenceAbove = Double.MAX_VALUE;
        double smallestDifferenceBelow = Double.MAX_VALUE;

        // Single loop to find both the closestAbove and closestBelow entries
        for (Map.Entry<Integer, Double> entry : this.normalizedBrightnessMap.entrySet()) {
            double difference = entry.getValue() - brightness;
            if (difference == 0) { // Exact match: Return immediately
                return (char) (int) entry.getKey();
            } else if (difference > 0 && difference < smallestDifferenceAbove) { // Check for closest above
                smallestDifferenceAbove = difference;
                closestAbove = entry;
            } else if (difference < 0 && -difference < smallestDifferenceBelow) { // Check for closest below
                smallestDifferenceBelow = -difference;
                closestBelow = entry;
            }
        }


        return getRoundedChar(closestAbove, closestBelow, brightness, roundType);
    }

    /**
     * Selects the ASCII character based on the closest brightness values and the specified rounding strategy.
     * We assume closestAbove and closestBelow will not be both null.
     *
     * @param closestAbove the closest entry with brightness above or equal to the target brightness,
     *                     or null if none.
     * @param closestBelow the closest entry with brightness below or equal to the target brightness,
     *                     or null if none.
     * @param brightness   the target normalized brightness value (between 0 and 1).
     * @param roundType    the rounding strategy to use:
     *                     - ABS: Select the character with the smallest absolute difference.
     *                     - UP: Select the closest character with brightness above or equal to brightness.
     *                     - DOWN: Select the closest character with brightness below or equal to brightness.
     * @return the ASCII character that matches the target brightness based on the rounding strategy.
     */
    private static char getRoundedChar(Map.Entry<Integer, Double> closestAbove,
                                       Map.Entry<Integer, Double> closestBelow,
                                       double brightness, RoundType roundType) {
        return switch (roundType) {
            case ABS -> {
                if (closestAbove != null && closestBelow != null) {
                    if (Math.abs(closestAbove.getValue() - brightness) <
                            Math.abs(closestBelow.getValue() - brightness)) {
                        yield (char) (int) closestAbove.getKey();
                    } else {
                        yield (char) (int) closestBelow.getKey();
                    }
                } else if (closestAbove != null) {
                    yield (char) (int) closestAbove.getKey();
                } else { // closestBelow is not null
                    yield (char) (int) closestBelow.getKey();
                }
            }
            case DOWN -> closestBelow != null ?
                    (char) (int) closestBelow.getKey() : (char) (int) closestAbove.getKey();
            case UP -> closestAbove != null ?
                    (char) (int) closestAbove.getKey() : (char) (int) closestBelow.getKey();
        };
    }

    /**
     * gets a char, and deletes the entry with the char as aa key, from both normalized and raw maps.
     *
     * @param c key in map to be removed
     */
    public void removeChar(char c) {
        double brightness = this.rawBrightnessMap.remove((int) c);
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

        for (double brightness : this.rawBrightnessMap.values()) {
            this.adjustMinAndMaxBrightness(brightness);
        }
    }

    /**
     * does a complete normalization operation on normalized map.
     * it's not needed to be called when minimum/brightness haven't changed after adding an ascii char.
     */
    private void normalizeAllValues() {
        for (Map.Entry<Integer, Double> entry : this.rawBrightnessMap.entrySet()) {
            double normalized = this.normalizeBrightness(entry.getValue());
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
    private double normalizeBrightness(double addedBrightness) {
        return (addedBrightness - this.minBrightness) / (this.maxBrightness - this.minBrightness);
    }

    /**
     * re-evaluates the maximum and minimum brightness in the class' ascii-char set.
     * the method is called smartly, only when adding an entry to the map where the brightness value
     * outreaches the current minimum and maximum.
     *
     * @param addedBrightness the new brightness value in the map that changes the min/max values.
     */
    private void adjustMinAndMaxBrightness(double addedBrightness) {
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