package ascii_art;

import image_char_matching.SubImgCharMatcher;

public class AsciiArtState {
    private final SubImgCharMatcher charMatcher;
    private final float[][] subImageBrightnessMap;

    public AsciiArtState(SubImgCharMatcher charMatcher, float[][] subImageBrightnessMap) {
        this.charMatcher = charMatcher;
        this.subImageBrightnessMap = subImageBrightnessMap;
    }

    public SubImgCharMatcher getCharMatcher() {
        return this.charMatcher;
    }

    public float[][] getBrightnessMap() {
        return subImageBrightnessMap;
    }
}