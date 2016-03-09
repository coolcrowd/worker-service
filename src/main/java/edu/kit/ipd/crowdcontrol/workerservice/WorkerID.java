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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerID)) return false;

        WorkerID workerID1 = (WorkerID) o;

        return workerID == workerID1.workerID;

    }

    @Override
    public int hashCode() {
        return workerID;
    }
}
