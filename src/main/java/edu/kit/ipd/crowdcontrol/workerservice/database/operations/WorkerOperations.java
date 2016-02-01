package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.InternalServerErrorException;
import org.jooq.DSLContext;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * The Worker-Operations is concerned with the Worker-Table
 * @author LeanderK
 * @version 1.0
 */
public class WorkerOperations extends AbstractOperation {
    /**
     * creates a new instance of WorkerOperations
     * @param create the context used to communicate with the database
     */
    public WorkerOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns whether the worker is under the Quality-Threshold
     * @param threshold the threshold
     * @param workerID the workerID
     * @return true if under threshold, false if not
     */
    public boolean isUnderThreshold(int threshold, int workerID) {
        return create.select(WORKER.QUALITY)
                .from(WORKER)
                .where(WORKER.ID_WORKER.eq(workerID))
                .fetchOptional()
                .orElseThrow(() -> new InternalServerErrorException(String.format("Worker %d is not existing", workerID)))
                .value1() < threshold;
    }
}
