package image_char_matching;

public class SubImgCharMatcher {

    private CharBrightnessMap brightnessMap;

    public SubImgCharMatcher(char[] charset) {
        this.brightnessMap = new CharBrightnessMap();
        for (char c : charset) {
            addChar(c);
        }
    }

    public char getCharByImageBrightness(double brightness){
        return brightnessMap.getCharByNormalizedBrightness(brightness);
    }

    public void addChar(char c){
        brightnessMap.addChar(c);
    }

    public void removeChar(char c){
        brightnessMap.removeChar(c);
    }
}
