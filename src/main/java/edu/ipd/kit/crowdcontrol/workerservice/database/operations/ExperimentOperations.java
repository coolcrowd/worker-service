package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import org.jooq.DSLContext;

/**
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentOperations extends AbstractOperation {
    public ExperimentOperations(DSLContext create) {
        super(create);
    }

    public ExperimentRecord getExperiment(int experimentID) throws ExperimentNotFoundException {
        return create.selectFrom(Tables.EXPERIMENT)
                .where(Tables.EXPERIMENT.IDEXPERIMENT.eq(experimentID))
                .fetchOptional()
                .orElseThrow(() -> new ExperimentNotFoundException(experimentID));
    }
}
