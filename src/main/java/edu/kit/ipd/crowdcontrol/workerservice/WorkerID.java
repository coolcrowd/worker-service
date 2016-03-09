package edu.kit.ipd.crowdcontrol.workerservice;

/**
 * this class holds the workerID
 * @author LeanderK
 * @version 1.0
 */
public class WorkerID {
    private final int workerID;

    public WorkerID(int workerID) {
        this.workerID = workerID;
    }

    public int get() {
        return workerID;
    }
}
