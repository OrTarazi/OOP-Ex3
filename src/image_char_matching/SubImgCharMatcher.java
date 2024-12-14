package image_char_matching;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Set;

public class SubImgCharMatcher {
    private final NavigableMap<Double, Set<Character>> brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        brightnessMap = new TreeMap<>();
        for (char c : charset) {
            addChar(c);
        }
    }

    public void addChar(char c) {
        boolean[][] boolArray = CharConverter.convertToBoolArray(c);
        double brightness = CharBrightnessCalculator.calculateCharBrightness(c);
        brightnessMap.computeIfAbsent(brightness, k -> new HashSet<>()).add(c);
    }

    public void removeChar(char c) {
        brightnessMap.values().forEach(set -> set.remove(c));
        brightnessMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public char getCharByImageBrightness(double brightness) {
        Map.Entry<Double, Set<Character>> lower = brightnessMap.floorEntry(brightness);
        Map.Entry<Double, Set<Character>> higher = brightnessMap.ceilingEntry(brightness);

        if (lower == null && higher == null) {
            throw new IllegalStateException("No characters in brightness map.");
        }

        if (lower == null || (higher != null && Math.abs(higher.getKey() - brightness) < Math.abs(lower.getKey() - brightness))) {
            return higher.getValue().iterator().next();
        } else {
            return lower.getValue().iterator().next();
        }
    }

}
