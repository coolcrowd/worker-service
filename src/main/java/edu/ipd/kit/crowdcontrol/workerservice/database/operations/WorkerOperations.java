package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import org.jooq.DSLContext;

/**
 * @author LeanderK
 * @version 1.0
 */
public class WorkerOperations extends AbstractOperation {

    public WorkerOperations(DSLContext create) {
        super(create);
    }

    public boolean hasEmail(int workerID) {
        return create.selectFrom(Tables.WORKER)
                .where(Tables.WORKER.IDWORKER.eq(workerID))
                .fetchOptional()
                .map(workerRecord -> !workerRecord.getEmail().isEmpty())
                .orElse(false);
    }

    public boolean exists(int workerID) {
        return create.selectFrom(Tables.WORKER)
                .where(Tables.WORKER.IDWORKER.eq(workerID))
                .fetchOptional()
                .map(workerRecord -> !workerRecord.getEmail().isEmpty())
                .orElse(false);
    }
}
