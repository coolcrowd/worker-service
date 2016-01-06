package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import org.jooq.DSLContext;

/**
 * WorkerOperations contains operations on the Worker.
 * @author LeanderK
 * @version 1.0
 */
public class WorkerOperations extends AbstractOperation {

    /**
     * creates a new instance of WorkerOperations.
     * @param create the context used to communicate with the database.
     */
    public WorkerOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns whether the Worker has an email
     * @param workerID the workerID
     * @return true if it has and email and ist existing, false if it has no email or the worker is not existing
     */
    public boolean hasEmail(int workerID) {
        return create.selectFrom(Tables.WORKER)
                .where(Tables.WORKER.IDWORKER.eq(workerID))
                .fetchOptional()
                .map(workerRecord -> !workerRecord.getEmail().isEmpty())
                .orElse(false);
    }

    /**
     * returns whether the workerID exists in the database
     * @param workerID the workerID to check for
     * @return true if exists, false if not
     */
    public boolean exists(int workerID) {
        return create.selectFrom(Tables.WORKER)
                .where(Tables.WORKER.IDWORKER.eq(workerID))
                .fetchOptional()
                .isPresent();
    }
}
