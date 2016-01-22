package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AlgorithmTaskChooserParamRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AlgorithmTaskChooserRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ConstraintRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.Map;

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
                .where(Tables.EXPERIMENT.ID_EXPERIMENT.eq(experimentID))
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

    /**
     * If a record with the given name already exists, the method returns false, if not it inserts a row and returns true.
     * @param name the name of the TaskChooser
     * @param description the description of the TaskChooser
     * @return true of inserted, false if already existing
     */
    public boolean insertTaskChooserOrIgnore(String name, String description) {
        AlgorithmTaskChooserRecord record = new AlgorithmTaskChooserRecord(name, description);
        return create.executeInsert(record) == 1;
    }

    /**
     * If a record with the passed parameters already exists, the method returns false, if not it inserts a row and
     * returns true.
     * @param taskChooser the referenced TaskChooser
     * @param description the description of the parameter
     * @param regex the regex the parameter mus be matching
     * @param data the additional data the algorithm wants to save or null
     * @return true if inserted, false if already existing
     */
    public boolean insertTaskChooserParamOrIgnore(String taskChooser, String description, String regex, String data) {
        boolean exists = create.fetchExists(
                DSL.selectFrom(Tables.ALGORITHM_TASK_CHOOSER_PARAM)
                        .where(Tables.ALGORITHM_TASK_CHOOSER_PARAM.ALGORITHM.eq(taskChooser))
                        .and(Tables.ALGORITHM_TASK_CHOOSER_PARAM.DESCRIPTION.eq(description))
                        .and(Tables.ALGORITHM_TASK_CHOOSER_PARAM.REGEX.eq(regex))
        );
        if (!exists) {
            return DSL.insertInto(Tables.ALGORITHM_TASK_CHOOSER_PARAM)
                    .set(new AlgorithmTaskChooserParamRecord(null, description, regex, taskChooser, data))
                    .execute() == 1;
        }
        return false;
    }

    /**
     * returns the amount of answers submitted for the passed experiment
     * @param experimentID the primary key of the experiment
     * @return the number of answers submitted
     */
    public int getAnswersCount(int experimentID) {
        //TODO duplicates?
        return create.fetchCount(
                DSL.selectFrom(Tables.ANSWER)
                        .where(Tables.ANSWER.EXPERIMENT.eq(experimentID))
        );
    }

    /**
     * returns the Task-Chooser Parameter for the experiment
     * @param experiment the primary key of the experiment to search for
     * @return the map with the description as the key and the parameter-value as value
     */
    public Map<String, String> getTaskChooserParam(int experiment) {
        return create.select(Tables.ALGORITHM_TASK_CHOOSER_PARAM.DATA, Tables.CHOOSEN_TASK_CHOOSER_PARAM.VALUE)
                .from(Tables.CHOOSEN_TASK_CHOOSER_PARAM)
                .join(Tables.ALGORITHM_TASK_CHOOSER_PARAM).onKey()
                .where(Tables.CHOOSEN_TASK_CHOOSER_PARAM.EXPERIMENT.eq(experiment))
                .fetchMap(Tables.ALGORITHM_TASK_CHOOSER_PARAM.DATA, Tables.CHOOSEN_TASK_CHOOSER_PARAM.VALUE);
    }
}
