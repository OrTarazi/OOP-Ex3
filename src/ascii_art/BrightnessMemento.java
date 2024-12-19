package ascii_art;

// todo: add documentation
class BrightnessMemento {
    private boolean lastStateValid = true;
    private double[][] brightnessHistory;

    // todo: add documentation
    public BrightnessMemento(double[][] subImageBrightnessMatrix) {
        this.saveState(subImageBrightnessMatrix);
    }

    // todo: add documentation
    public boolean isLastStateValid() {
        return this.lastStateValid;
    }

    // todo: add documentation
    public void setLastStateValidity(boolean validity) {
        this.lastStateValid = validity;
    }

    // todo: add documentation
    // Save the current state of the originator
    public void saveState(double[][] subImageBrightnessMatrix) {
        this.brightnessHistory = subImageBrightnessMatrix;
    }

    // todo: add documentation
    // Restore the last saved state of the originator
    public double[][] restoreState() {
        return this.brightnessHistory;
    }
}