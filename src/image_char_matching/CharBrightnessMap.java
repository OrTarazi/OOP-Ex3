package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

//TODO: add documentation
public class CharBrightnessMap {
    // Stores raw brightness values
    private final TreeMap<Integer, Float> rawBrightnessMap;
    // Stores normalized brightness values
    // TODO: init min and max initial values with constants in constructor
    private float minBrightness = 1;
    private final TreeMap<Integer, Float> normalizedBrightnessMap;
    private float maxBrightness = 0;

    public CharBrightnessMap() {
        this.rawBrightnessMap = new TreeMap<>();
        this.normalizedBrightnessMap = new TreeMap<>();
    }

    public void addChar(char c) {
        float brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        this.rawBrightnessMap.put((int) c, brightness); // Always store raw brightness

        // TODO: Optimize conditions
        if (this.rawBrightnessMap.size() > 1 &&
                (brightness > this.maxBrightness || brightness < this.minBrightness)) {
            this.adjustMinAndMaxBrightness(brightness);
            this.normalizedBrightnessMap.put((int) c, this.normalizeBrightness(brightness));
            this.normalizeAllValues();
        } else if (this.rawBrightnessMap.size() > 1) {
            this.normalizedBrightnessMap.put((int) c, this.normalizeBrightness(brightness));
        } else {
            this.adjustMinAndMaxBrightness(brightness);
        }
    }

    // TODO: check if getters are needed
    // TODO: -1 should be a constant
    public float getRawBrightness(char c) {
        return this.rawBrightnessMap.getOrDefault((int) c, -1.0f);
    }

    // TODO: -1 should be a constant
    public float getNormalizedBrightness(char c) {
        return this.normalizedBrightnessMap.getOrDefault((int) c, -1.0f);
    }

    public char getCharByNormalizedBrightness(float brightness) {
        Map.Entry<Integer, Float> closest = null;
        float smallestDifference = Float.MAX_VALUE;

        for (Map.Entry<Integer, Float> entry : this.normalizedBrightnessMap.entrySet()) {
            // TODO: change round in main
            float difference = Math.abs(entry.getValue() - brightness);

            if (difference < smallestDifference) {
                smallestDifference = difference;
                closest = entry;
            }
        }

        if (closest == null) {
            throw new IllegalStateException("No characters in brightness map.");
        }

        return (char) (int) closest.getKey();
    }

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
    // TODO: make sure we understand and handle exceptions

    private void recalculateMinMaxBrightness() {
        // TODO: init min and max initial values with constants
        this.minBrightness = 1;
        this.maxBrightness = 0;

        for (float brightness : this.rawBrightnessMap.values()) {
            this.adjustMinAndMaxBrightness(brightness);
        }
    }

    private void normalizeAllValues() {
        this.normalizedBrightnessMap.clear();

        for (Map.Entry<Integer, Float> entry : this.rawBrightnessMap.entrySet()) {
            float normalized = this.normalizeBrightness(entry.getValue());
            // TODO: check replacing value instead of clear and put
            this.normalizedBrightnessMap.put(entry.getKey(), normalized);
        }
    }

    private float normalizeBrightness(float brightness) {
        // TODO: Assumption, condition can br removed (check)
        // Avoid division by zero when all values are identical
        if (this.minBrightness == this.maxBrightness) {
            return brightness;
        }

        return (brightness - this.minBrightness) / (this.maxBrightness - this.minBrightness);
    }

    private void adjustMinAndMaxBrightness(float brightness) {
        if (brightness < this.minBrightness) {
            this.minBrightness = brightness;
        }

        if (brightness > this.maxBrightness) {
            this.maxBrightness = brightness;
        }
    }
}