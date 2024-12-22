package ascii_art;

/**
 * A memento-like class for storing and restoring sub-image brightnesses that are calculated once.
 * AsciiArtAlgorithm holds an instance of BrightnessMemento, and uses it to update it (save) when
 * calculations are made, and if run parameters haven't changed - would be restored and re-used.
 * @author Or Tarazi, Agam Hershko
 */
class BrightnessMemento {
    private boolean lastStateValid = true;
    private double[][] brightnessHistory;

    /**
     *  creates a new instance of the memento object.
     * @param subImageBrightnessMatrix the initial brightnesses to be saved
     */
    public BrightnessMemento(double[][] subImageBrightnessMatrix) {
        this.saveState(subImageBrightnessMatrix);
    }


    /**
     * get the validity status of the last state
     * @return last state validity
     */
    public boolean isLastStateValid() {
        return this.lastStateValid;
    }

    /**
     * set the validity status of the last state
     */
    public void setLastStateValidity(boolean validity) {
        this.lastStateValid = validity;
    }

    /**
     * stores the brightnesses of sub-images
     * @param subImageBrightnessMatrix the sub-images brightnesses matrix to be stored in memento
     */

    public void saveState(double[][] subImageBrightnessMatrix) {
        this.brightnessHistory = subImageBrightnessMatrix;
    }

    /**
     * on demand, returns the brightnesses from earlier
     * @return saved brightnesses from last run
     */
    public double[][] restoreState() {
        return this.brightnessHistory;
    }
}