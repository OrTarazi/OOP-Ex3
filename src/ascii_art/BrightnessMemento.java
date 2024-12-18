package ascii_art;

class BrightnessMemento {
    private boolean lastStateValid = true;
    private float[][] brightnessHistory;

    public BrightnessMemento(float [][] subImageBrightnessMatrix) {
        this.saveState(subImageBrightnessMatrix);
    }

    public boolean isLastStateValid(){return lastStateValid;}

    public void setLastStateValidity(boolean validity){lastStateValid = validity;}

    // Save the current state of the originator
    public void saveState(float [][] subImageBrightnessMatrix) {
        this.brightnessHistory = subImageBrightnessMatrix;
    }

    // Restore the last saved state of the originator
    public float[][] restoreState() {
        return this.brightnessHistory;
    }

}