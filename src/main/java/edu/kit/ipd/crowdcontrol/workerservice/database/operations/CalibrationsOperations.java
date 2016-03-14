package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationAnswerOptionRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.CalibrationRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Map;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * CalibrationsOperations contains queries which are concerned with calibrations.
 * @author LeanderK
 * @version 1.0
 */
public class CalibrationsOperations extends AbstractOperation {

    /**
     * creates a new instance of CalibrationsOperations
     * @param create the context used to communicate with the database
     * @param cacheEnabled whether the caching functionality should be enabled
     */
    public CalibrationsOperations(DSLContext create, boolean cacheEnabled) {
        super(create, cacheEnabled);
    }

    /**
     * returns all the open Calibrations needed from the worker
     * @param experimentID the experiment
     * @param platformID the id of the platform
     * @param worker the worker
     * @return a map where the keys are the detailed population the worker may belong to and the values are the answerOptions
     */
    public Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> getCalibrations(int experimentID, String platformID, int worker) {
        SelectConditionStep<Record1<Integer>> alreadyAnsweredCalibrations = DSL.select(CALIBRATION_ANSWER_OPTION.CALIBRATION)
                .from(Tables.CALIBRATION_ANSWER_OPTION)
                .join(Tables.CALIBRATION_RESULT).onKey()
                .where(Tables.CALIBRATION_RESULT.WORKER.eq(worker));

        Map<CalibrationRecord, Result<Record>> calibrationAndAnswers = create.select(CALIBRATION.fields())
                .select(CALIBRATION_ANSWER_OPTION.fields())
                .from(Tables.CALIBRATION_ANSWER_OPTION)
                .join(Tables.CALIBRATION).onKey()
                //we want all the calibrations
                .where(CALIBRATION.ID_CALIBRATION.in(
                        //which belong to the chosen answer-option
                        DSL.select(CALIBRATION_ANSWER_OPTION.CALIBRATION)
                            .from(CALIBRATION_ANSWER_OPTION)
                            .where(CALIBRATION_ANSWER_OPTION.ID_CALIBRATION_ANSWER_OPTION.in(
                                    //from the experiment
                                    DSL.select(EXPERIMENTS_CALIBRATION.ANSWER)
                                        .from(EXPERIMENTS_CALIBRATION)
                                        .where(EXPERIMENTS_CALIBRATION.EXPERIMENTS_PLATFORM.eq(
                                                //from the platform
                                                DSL.select(EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS)
                                                        .from(EXPERIMENTS_PLATFORM)
                                                        .where(EXPERIMENTS_PLATFORM.EXPERIMENT.eq(experimentID))
                                                        .and(EXPERIMENTS_PLATFORM.PLATFORM.eq(platformID))
                                        ))
                            ))
                ))
                .and(Tables.CALIBRATION.ID_CALIBRATION.notIn(alreadyAnsweredCalibrations))
                .fetchGroups(Tables.CALIBRATION);

        return mapMap(calibrationAndAnswers, result -> result.into(Tables.CALIBRATION_ANSWER_OPTION.asTable()));
    }

    /**
     * returns whether the worker already has submitted the wrong calibrations
     * @param experimentID the current experiment
     * @param platformID the platform the worker is working on
     * @param worker the worker to check for
     * @return true if the worker is already belonging to the wrong population, false if not
     */
    public boolean hasSubmittedWrongCalibrations(int experimentID, String platformID, int worker) {
        SelectConditionStep<Record1<Integer>> getCalibrationForExperiment = DSL.select(CALIBRATION_ANSWER_OPTION.CALIBRATION)
                .from(CALIBRATION_ANSWER_OPTION)
                .where(CALIBRATION_ANSWER_OPTION.ID_CALIBRATION_ANSWER_OPTION.eq(EXPERIMENTS_CALIBRATION.ANSWER));

        SelectConditionStep<Record1<Integer>> getExperimentPlatformIds = DSL.select(EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS)
                .from(EXPERIMENTS_PLATFORM)
                .where(EXPERIMENTS_PLATFORM.EXPERIMENT.eq(experimentID))
                .and(EXPERIMENTS_PLATFORM.PLATFORM.eq(platformID));

        return create.fetchExists(
                DSL.select()
                    .from(CALIBRATION_ANSWER_OPTION)
                    .innerJoin(CALIBRATION_RESULT).onKey()
                    .innerJoin(EXPERIMENTS_CALIBRATION).on(
                        EXPERIMENTS_CALIBRATION.EXPERIMENTS_PLATFORM.in(getExperimentPlatformIds)
                        .and(CALIBRATION_ANSWER_OPTION.CALIBRATION.eq(getCalibrationForExperiment))
                        .and(EXPERIMENTS_CALIBRATION.NOT.eq(true).and(EXPERIMENTS_CALIBRATION.ANSWER.eq(CALIBRATION_ANSWER_OPTION.ID_CALIBRATION_ANSWER_OPTION))
                            .or(EXPERIMENTS_CALIBRATION.NOT.eq(false).and(EXPERIMENTS_CALIBRATION.ANSWER.notEqual(CALIBRATION_ANSWER_OPTION.ID_CALIBRATION_ANSWER_OPTION))))
                    )
                        .where(CALIBRATION_RESULT.WORKER.eq(worker))
        );
    }
}
