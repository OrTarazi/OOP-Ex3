package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

//TODO: add documentation
// TODO: add this.
public class CharBrightnessMap {
    // Stores raw brightness values
    private final TreeMap<Integer, Float> rawBrightnessMap;
    // Stores normalized brightness values
    // TODO: init min and max initial values with constants in constructor
    private float minBrightness = 1;
    private final TreeMap<Integer, Float> normalizedBrightnessMap;
    private float maxBrightness = 0;

    public CharBrightnessMap() {
        rawBrightnessMap = new TreeMap<>();
        normalizedBrightnessMap = new TreeMap<>();
    }

    public void addChar(char c) {
        float brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        rawBrightnessMap.put((int) c, brightness); // Always store raw brightness

        // TODO: Reduce duplication
        if (rawBrightnessMap.size() > 1 && (brightness > maxBrightness || brightness < minBrightness)) {
            adjustMinAndMaxBrightness(brightness);
            normalizedBrightnessMap.put((int) c, normalizeBrightness(brightness));
            normalizeAllValues();
        } else if (rawBrightnessMap.size() > 1) {
            normalizedBrightnessMap.put((int) c, normalizeBrightness(brightness));
        }
    }

    // TODO: check if getters are needed
    // TODO: -1 should be a constant
    public float getRawBrightness(char c) {
        return rawBrightnessMap.getOrDefault((int) c, -1.0f);
    }

    // TODO: -1 should be a constant
    public float getNormalizedBrightness(char c) {
        return normalizedBrightnessMap.getOrDefault((int) c, -1.0f);
    }

    public void removeChar(char c) {
        float brightness = rawBrightnessMap.remove((int) c);
        normalizedBrightnessMap.remove((int) c);
        if (brightness == maxBrightness || brightness == minBrightness) {
            recalculateMinMaxBrightness();
            if (rawBrightnessMap.size() > 1) {
                normalizeAllValues();
            }
        }

    }

    // TODO: make sure we understand and handle exceptions
    public char getCharByNormalizedBrightness(float brightness) {
        Map.Entry<Integer, Float> lower = normalizedBrightnessMap.floorEntry((int) brightness);
        Map.Entry<Integer, Float> higher = normalizedBrightnessMap.ceilingEntry((int) brightness);

        if (lower == null && higher == null) {
            throw new IllegalStateException("No characters in brightness map.");
        }

        if (lower == null || (higher != null && Math.abs(higher.getValue() - brightness) < Math.abs(lower.getValue() - brightness))) {
            return (char) (int) higher.getKey();
        } else {
            return (char) (int) lower.getKey();
        }
    }

    private void normalizeAllValues() {
        normalizedBrightnessMap.clear();
        for (Map.Entry<Integer, Float> entry : rawBrightnessMap.entrySet()) {
            float normalized = normalizeBrightness(entry.getValue());
            // TODO: check complexity
            normalizedBrightnessMap.put(entry.getKey(), normalized);
        }
    }

    private void adjustMinAndMaxBrightness(float brightness) {
        if (brightness < minBrightness) {
            minBrightness = brightness;
        } else if (brightness > maxBrightness) {
            maxBrightness = brightness;
        }
    }

    private void recalculateMinMaxBrightness() {
        minBrightness = 1;
        maxBrightness = 0;

        for (float brightness : rawBrightnessMap.values()) {
            adjustMinAndMaxBrightness(brightness);
        }
    }

    private float normalizeBrightness(float brightness) {
        if (minBrightness == maxBrightness) {
            return brightness; // Avoid division by zero when all values are identical
        }
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }
}
