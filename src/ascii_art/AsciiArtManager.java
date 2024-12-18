package ascii_art;

import image_char_matching.SubImgCharMatcher;

class AsciiArtManager {
    private boolean lastStateValid = true;
    AsciiArtState lastState;

    public AsciiArtManager(SubImgCharMatcher charMatcher, float [][] subImageBrightnessMatrix) {
        this.saveState(charMatcher, subImageBrightnessMatrix);
    }

    public boolean isLastStateValid(){return lastStateValid;}

    public void setLastStateValidity(boolean validity){lastStateValid = validity;}

    // Save the current state of the originator
    public void saveState(SubImgCharMatcher charMatcher, float [][] subImageBrightnessMatrix) {
        this.lastState = new AsciiArtState(charMatcher, subImageBrightnessMatrix);
    }

    // Restore the last saved state of the originator
    public AsciiArtState restoreState() {
        return this.lastState;
    }

}