package image_char_matching;

import java.util.Map;
import java.util.TreeMap;

public class CharBrightnessMap {
    private final TreeMap<Integer, Double> rawBrightnessMap;       // Stores raw brightness values
    private final TreeMap<Integer, Double> normalizedBrightnessMap; // Stores normalized brightness values
    private double minBrightness = 1;
    private double maxBrightness = 0;

    public CharBrightnessMap() {
        rawBrightnessMap = new TreeMap<>();
        normalizedBrightnessMap = new TreeMap<>();
    }

    public void addChar(char c) {
        double brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        rawBrightnessMap.put((int) c, brightness); // Always store raw brightness

        // TODO try to
        if (rawBrightnessMap.size() > 1 && (brightness>maxBrightness || brightness<minBrightness))
        {
            adjustMinAndMaxBrightness(brightness);
            normalizedBrightnessMap.put((int) c, normalizeBrightness(brightness));
            normalizeAllValues();

        }
        else if (rawBrightnessMap.size() > 1)
        {
            normalizedBrightnessMap.put((int) c, normalizeBrightness(brightness));
        }
    }

    public double getRawBrightness(char c) {
        return rawBrightnessMap.getOrDefault((int) c, -1.0);
    }

    public double getNormalizedBrightness(char c) {
        return normalizedBrightnessMap.getOrDefault((int) c, -1.0);
    }

    public void removeChar(char c) {
        double brightness = rawBrightnessMap.remove((int) c);
        normalizedBrightnessMap.remove((int) c);
        if (brightness == maxBrightness || brightness == minBrightness){
            recalculateMinMaxBrightness();
            if (rawBrightnessMap.size() > 1) {
                normalizeAllValues();
            }
        }

    }

    public char getCharByNormalizedBrightness(double brightness) {
        Map.Entry<Integer, Double> lower = normalizedBrightnessMap.floorEntry((int) brightness);
        Map.Entry<Integer, Double> higher = normalizedBrightnessMap.ceilingEntry((int) brightness);

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
        for (Map.Entry<Integer, Double> entry : rawBrightnessMap.entrySet()) {
            double normalized = normalizeBrightness(entry.getValue());
            normalizedBrightnessMap.put(entry.getKey(), normalized);
        }
    }

    private void adjustMinAndMaxBrightness(double brightness) {
        if (brightness < minBrightness) {
            minBrightness = brightness;
        }
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
        }
    }

    private void recalculateMinMaxBrightness() {
        minBrightness = 1;
        maxBrightness = 0;

        for (double brightness : rawBrightnessMap.values()) {
            adjustMinAndMaxBrightness(brightness);
        }
    }

    private double normalizeBrightness(double brightness) {
        if (minBrightness == maxBrightness) {
            return brightness; // Avoid division by zero when all values are identical
        }
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }
}
