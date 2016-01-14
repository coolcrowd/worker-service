package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ConstraintRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import org.jooq.DSLContext;
import org.jooq.Result;

/**
 * contains all the operations concerned with experiments.
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentOperations extends AbstractOperation {
    /**
     * creates a new ExperimentOperations
     * @param create the Context used to communicate with the database
     */
    public ExperimentOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns an ExperimentRecord corresponding to the experimentID or throws an ExperimentNotFoundException.
     * @param experimentID the ID of the experiment
     * @return an instance of ExperimentRecord
     * @throws ExperimentNotFoundException if no matching experiment was found in the database
     */
    public ExperimentRecord getExperiment(int experimentID) throws ExperimentNotFoundException {
        return create.selectFrom(Tables.EXPERIMENT)
                .where(Tables.EXPERIMENT.IDEXPERIMENT.eq(experimentID))
                .fetchOptional()
                .orElseThrow(() -> new ExperimentNotFoundException(experimentID));
    }

    /**
     * returns all the Constraints associated with the Experiment-ID
     * @param experimentID the experiment-ID
     * @return the resulting Records
     */
    public Result<ConstraintRecord> getConstraints(int experimentID) {
        return create.selectFrom(Tables.CONSTRAINT)
                .where(Tables.CONSTRAINT.EXPERIMENT.eq(experimentID))
                .fetch();
    }
}
