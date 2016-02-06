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
     */
    public CalibrationsOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns all the open Calibrations needed from the worker
     * @param experimentID the experiment
     * @param platformID the id of the platform
     * @param worker the worker
     * @return a map where the keys are the detailed population the worker may belong to and the values are the answerOptions
     */
    public Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> getCalibrations(int experimentID, String platformID, int worker) {
        SelectConditionStep<Record1<Integer>> answered = DSL.select(CALIBRATION_RESULT.ANSWER)
                .from(CALIBRATION_RESULT)
                .where(CALIBRATION_RESULT.WORKER.eq(worker));


        SelectConditionStep<Record1<Integer>> alreadyAnsweredCalibrations = DSL.select(CALIBRATION_ANSWER_OPTION.CALIBRATION)
                .from(Tables.CALIBRATION_ANSWER_OPTION)
                .join(Tables.CALIBRATION_RESULT).onKey()
                .where(Tables.CALIBRATION_RESULT.ID_CALIBRATION_RESULT.in(answered));

        Map<CalibrationRecord, Result<Record>> calibrationAndAnswers = create.select(CALIBRATION.fields())
                .select(CALIBRATION_ANSWER_OPTION.fields())
                .from(Tables.CALIBRATION_ANSWER_OPTION)
                .join(Tables.CALIBRATION).onKey()
                .join(Tables.EXPERIMENTS_CALIBRATION).on(EXPERIMENTS_CALIBRATION.ANSWER.eq(CALIBRATION_ANSWER_OPTION.ID_CALIBRATION_ANSWER_OPTION))
                .where(Tables.EXPERIMENTS_CALIBRATION.ID_EXPERIMENTS_CALIBRATION.eq(experimentID))
                .and(Tables.EXPERIMENTS_CALIBRATION.REFERENCED_PLATFORM.eq(platformID))
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
        SelectConditionStep<Record1<Integer>> answered = DSL.select(Tables.CALIBRATION_RESULT.ANSWER)
                .from(Tables.CALIBRATION_RESULT)
                .where(Tables.CALIBRATION_RESULT.WORKER.eq(worker));

        return create.fetchExists(DSL.selectFrom(Tables.EXPERIMENTS_CALIBRATION)
                .where(Tables.EXPERIMENTS_CALIBRATION.ID_EXPERIMENTS_CALIBRATION.eq(experimentID))
                .and(Tables.EXPERIMENTS_CALIBRATION.REFERENCED_PLATFORM.eq(platformID))
                .and(Tables.EXPERIMENTS_CALIBRATION.NOT.eq(false).and(Tables.EXPERIMENTS_CALIBRATION.ANSWER.notIn(answered))
                        .or(Tables.EXPERIMENTS_CALIBRATION.NOT.eq(true).and(Tables.EXPERIMENTS_CALIBRATION.ANSWER.in(answered)))));
    }
}