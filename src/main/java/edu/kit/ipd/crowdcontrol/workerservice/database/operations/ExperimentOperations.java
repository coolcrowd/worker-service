package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import com.google.common.cache.LoadingCache;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.*;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * contains all the operations concerned with experiments.
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentOperations extends AbstractOperation {
    private LoadingCache<Integer, ExperimentRecord> experimentCache = createCache(this::getExperimentFromDB);
    private LoadingCache<Integer, Result<ConstraintRecord>> constrainsCache = createCache(this::getConstraintsFromDB);
    private LoadingCache<Integer, List<RatingOptionExperimentRecord>> ratingOptionsCache = createCache(this::getRatingOptionsFromDB);
    private LoadingCache<Integer, Map<String, String>> taskChooserParamCache = createCache(this::getTaskChooserParamFromDB);

    /**
     * creates a new ExperimentOperations
     * @param create the Context used to communicate with the database
     * @param cacheEnabled whether the caching functionality should be enabled
     */
    public ExperimentOperations(DSLContext create, boolean cacheEnabled) {
        super(create, cacheEnabled);
    }

    /**
     * returns an ExperimentRecord corresponding to the experimentID or throws an ExperimentNotFoundException.
     * <p>
     * this method is cached.
     * @param experimentID the ID of the experiment
     * @return an instance of ExperimentRecord
     * @throws ExperimentNotFoundException if no matching experiment was found in the database
     */
    public ExperimentRecord getExperiment(int experimentID) throws ExperimentNotFoundException {
        return cacheGetHelper(experimentCache, experimentID,
                key -> String.format("unable to load the Experiment %d from the database", key));
    }

    /**
     * returns an ExperimentRecord corresponding to the experimentID or throws an ExperimentNotFoundException.
     * <p>
     * this method accesses the db.
     * @param experimentID the ID of the experiment
     * @return an instance of ExperimentRecord
     * @throws ExperimentNotFoundException if no matching experiment was found in the database
     */
    private ExperimentRecord getExperimentFromDB(int experimentID) throws ExperimentNotFoundException {
        return create.selectFrom(Tables.EXPERIMENT)
                .where(Tables.EXPERIMENT.ID_EXPERIMENT.eq(experimentID))
                .fetchOptional()
                .orElseThrow(() -> new ExperimentNotFoundException(experimentID));
    }

    /**
     * returns all the Constraints associated with the Experiment-ID
     * <p>
     * this method is cached.
     * @param experimentID the experiment-ID
     * @return the resulting Records
     */
    public Result<ConstraintRecord> getConstraints(int experimentID) {
        return cacheGetHelper(constrainsCache, experimentID,
                key -> String.format("unable to load the constraints for experiment %d from the database", key));
    }

    /**
     * returns all the Constraints associated with the Experiment-ID
     * <p>
     * this method accesses the db.
     * @param experimentID the experiment-ID
     * @return the resulting Records
     */
    private Result<ConstraintRecord> getConstraintsFromDB(int experimentID) {
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
        return create.insertInto(Tables.ALGORITHM_TASK_CHOOSER)
                .set(record)
                .onDuplicateKeyUpdate()
                .set(record)
                .execute() == 1;
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
            return create.insertInto(Tables.ALGORITHM_TASK_CHOOSER_PARAM)
                    .set(new AlgorithmTaskChooserParamRecord(null, description, regex, taskChooser, data))
                    .execute() == 1;
        }
        return false;
    }

    /**
     * returns the Task-Chooser Parameter for the experiment
     * <p>
     * this method is cached
     * @param experiment the primary key of the experiment to search for
     * @return the map with the description as the key and the parameter-value as value
     */
    public Map<String, String> getTaskChooserParam(int experiment) {
        return cacheGetHelper(taskChooserParamCache, experiment,
                key -> String.format("no taskChooser found for experiment %d ", key));
    }

    /**
     * returns the Task-Chooser Parameter for the experiment
     * <p>
     * this method accesses the db.
     * @param experiment the primary key of the experiment to search for
     * @return the map with the description as the key and the parameter-value as value
     */
    private Map<String, String> getTaskChooserParamFromDB(int experiment) {
        return create.select(Tables.ALGORITHM_TASK_CHOOSER_PARAM.DATA, Tables.CHOSEN_TASK_CHOOSER_PARAM.VALUE)
                .from(Tables.CHOSEN_TASK_CHOOSER_PARAM)
                .join(Tables.ALGORITHM_TASK_CHOOSER_PARAM).onKey()
                .where(Tables.CHOSEN_TASK_CHOOSER_PARAM.EXPERIMENT.eq(experiment))
                .fetchMap(Tables.ALGORITHM_TASK_CHOOSER_PARAM.DATA, Tables.CHOSEN_TASK_CHOOSER_PARAM.VALUE);
    }

    /**
     * returns the Rating-Options for the experiment.
     * <p>
     * this method is cached.
     * @param experiment the primary key of the experiment
     * @return a list of RatingOptions
     */
    public List<RatingOptionExperimentRecord> getRatingOptions(int experiment) {
        return cacheGetHelper(ratingOptionsCache, experiment,
                key -> String.format("unable to get RatingOptions for Experiment %d from db", key));
    }

    /**
     * returns the Rating-Options for the experiment.
     * <p>
     * this method accesses the db.
     * @param experiment the primary key of the experiment
     * @return a list of RatingOptions
     */
    private List<RatingOptionExperimentRecord> getRatingOptionsFromDB(int experiment) {
        return create.selectFrom(Tables.RATING_OPTION_EXPERIMENT)
                .where(Tables.RATING_OPTION_EXPERIMENT.EXPERIMENT.eq(experiment))
                .fetch();
    }

    /**
     * returns all the running experiments for the platforms
     * @param platform the active platform
     * @return the running experiments
     */
    public Result<Record3<Integer, String, String>> getRunningExperimentsForPlatform(String platform) {
        ExperimentsPlatformStatus status1 = EXPERIMENTS_PLATFORM_STATUS.as("mode1");
        ExperimentsPlatformStatus status2 = EXPERIMENTS_PLATFORM_STATUS.as("mode2");
        List<ExperimentsPlatformStatusPlatformStatus> validStates = Arrays.asList(
                ExperimentsPlatformStatusPlatformStatus.running,
                ExperimentsPlatformStatusPlatformStatus.creative_stopping
        );
        SelectConditionStep<Record1<Integer>> experiments = create.select(EXPERIMENTS_PLATFORM.EXPERIMENT)
                .from(EXPERIMENTS_PLATFORM)
                .join(status1).on(
                        EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS.eq(status1.PLATFORM)
                                .and(status1.PLATFORM_STATUS.in(validStates))
                )
                .leftOuterJoin(status2).on(
                        EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS.eq(status2.PLATFORM)
                                .and(status1.TIMESTAMP.lessThan(status2.TIMESTAMP).or(status1.TIMESTAMP.eq(status2.TIMESTAMP)
                                        .and(status1.ID_EXPERIMENTS_PLATFORM_STATUS.lessThan(status2.ID_EXPERIMENTS_PLATFORM_STATUS))))
                                .and(status2.PLATFORM_STATUS.in(validStates))
                )
                .where(status2.ID_EXPERIMENTS_PLATFORM_STATUS.isNull())
                .and(EXPERIMENTS_PLATFORM.PLATFORM.eq(platform));

        return create.select(EXPERIMENT.ID_EXPERIMENT, EXPERIMENT.TITLE, EXPERIMENT.DESCRIPTION)
                .from(EXPERIMENT)
                .where(EXPERIMENT.ID_EXPERIMENT.in(
                        DSL.select(EXPERIMENTS_PLATFORM.EXPERIMENT)
                                .from(EXPERIMENTS_PLATFORM)
                                .where(EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS.in(
                                        experiments
                                ))
                ))
                .fetch();

    }
}
