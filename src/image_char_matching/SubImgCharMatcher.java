package image_char_matching;

// TODO: Add documentation
public class SubImgCharMatcher {

    private CharBrightnessMap brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        this.brightnessMap = new CharBrightnessMap();
        for (char c : charset) {
            this.addChar(c);
        }
    }

    public char getCharByImageBrightness(float brightness) {
        return this.brightnessMap.getCharByNormalizedBrightness(brightness);
    }

    public void addChar(char c) {
        this.brightnessMap.addChar(c);
    }

    public void removeChar(char c) {
        this.brightnessMap.removeChar(c);
    }
}