package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.WorkerRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * @author LeanderK
 * @version 1.0
 */
public class WorkerOperations extends AbstractOperation {
    private final ExperimentOperations experimentOperations;

    public WorkerOperations(DSLContext create, ExperimentOperations experimentOperations) {
        super(create);
        this.experimentOperations = experimentOperations;
    }

    public int createWorker(int platform) {
        return create.insertInto(Tables.WORKER)
                .set(new WorkerRecord(null, null, platform, null))
                .returning()
                .fetchOne()
                .getIdworker();
    }

    public int mergeWorker(int originalID, int newID) {
        return create.transactionResult(conf -> {
            int answers = DSL.using(conf).update(Tables.ANSWERS)
                    .set(Tables.ANSWERS.WORKER_ID, newID)
                    .where(Tables.ANSWERS.WORKER_ID.eq(originalID))
                    .execute();
            int ratings = DSL.using(conf).update(Tables.RATINGS)
                    .set(Tables.RATINGS.WORKER_ID, newID)
                    .where(Tables.RATINGS.WORKER_ID.eq(originalID))
                    .execute();
            return answers + ratings;
        });
    }
}
