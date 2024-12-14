package image_char_matching;
import java.util.Map;
import java.util.TreeMap;

public class CharBrightnessMap {
    private final TreeMap<Integer, Double> brightnessMap;
    private double minBrightness = 0;
    private double maxBrightness = 0;

    public CharBrightnessMap() {
        brightnessMap = new TreeMap<>();
    }

    public void addCharBrightness(char c) {
        double brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        if (brightness < minBrightness) {
            minBrightness = brightness;
        }
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
        }

        brightnessMap.put((int) c, brightness);
    }

    public double getBrightness(char c) {
        return brightnessMap.getOrDefault((int) c, -1.0);
    }


    public double getNormalizedBrightness(char c) {
        double rawBrightness = getBrightness(c);
        if (rawBrightness == -1.0) {
            return -1.0; // not found
        }
        return normalizeBrightness(rawBrightness);
    }

    public void removeChar(char c) {
        Double brightness = brightnessMap.remove((int) c);

        if (brightness != null) {

            if (brightness == minBrightness || brightness == maxBrightness) {
                recalculateMinMaxBrightness();
            }
        }
    }

    /**
     * Does a search in the map for a given brightness value, and returns the closest matching character.
     * @param brightness the brightness value to match.
     * @return the character that best matches the provided brightness.
     */
    public char getCharByBrightness(double brightness) {
        // Todo check if these two below actually run in O(log n):
        Map.Entry<Integer, Double> lower = brightnessMap.floorEntry((int) brightness);
        Map.Entry<Integer, Double> higher = brightnessMap.ceilingEntry((int) brightness);

        if (lower == null && higher == null) {
            throw new IllegalStateException("No characters in brightness map.");
        }

        if (lower == null || (higher != null && Math.abs(higher.getValue() - brightness) < Math.abs(lower.getValue() - brightness))) {
            return (char) (int) higher.getKey();
        } else {
            return (char) (int) lower.getKey();
        }
    }


    public double normalizeBrightness(double charBrightness) {

        if (maxBrightness == minBrightness) {
            return 0.0;
        }
        return (charBrightness - minBrightness) / (maxBrightness - minBrightness);
    }

    private void recalculateMinMaxBrightness() {
        minBrightness = 0;
        maxBrightness = 0;

        for (double brightness : brightnessMap.values()) {
            if (brightness < minBrightness) {
                minBrightness = brightness;
            }
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
            }
        }
    }
}
