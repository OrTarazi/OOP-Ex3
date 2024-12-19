package ascii_art;

// todo: fix doc
class BrightnessMemento {
    private boolean lastStateValid = true;
    private float[][] brightnessHistory;

    // todo: fix doc
    public BrightnessMemento(float[][] subImageBrightnessMatrix) {
        this.saveState(subImageBrightnessMatrix);
    }

    // todo: fix doc
    public boolean isLastStateValid() {
        return this.lastStateValid;
    }

    // todo: fix doc
    public void setLastStateValidity(boolean validity) {
        this.lastStateValid = validity;
    }

    // todo: fix doc
    // Save the current state of the originator
    public void saveState(float[][] subImageBrightnessMatrix) {
        this.brightnessHistory = subImageBrightnessMatrix;
    }

    // todo: fix doc
    // Restore the last saved state of the originator
    public float[][] restoreState() {
        return this.brightnessHistory;
    }
}