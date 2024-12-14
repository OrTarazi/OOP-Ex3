package image_char_matching;

public class SubImgCharMatcher {
    private final CharBrightnessMap brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        brightnessMap = new CharBrightnessMap();
        for (char c : charset) {
            addChar(c);
        }
    }

    // Adds a character to the map by calculating its brightness
    public void addChar(char c) {
        brightnessMap.addCharBrightness(c);
    }

    // Removes a character from the map
    public void removeChar(char c) {
        brightnessMap.removeChar(c);
    }

    // Retrieves the character whose brightness is closest to the given value
    public char getCharByImageBrightness(double brightness) {
        return brightnessMap.getCharByBrightness(brightness);
    }
}
