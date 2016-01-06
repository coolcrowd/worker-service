package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Map;

import static edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables.*;

/**
 *
 * @author LeanderK
 * @version 1.0
 */
public class PopulationsOperations extends AbstractOperation {
    private final PlatformOperations platformOperations;

    public PopulationsOperations(DSLContext create, PlatformOperations platformOperations) {
        super(create);
        this.platformOperations = platformOperations;
    }

    /**
     * returns all the open Calibrations needed from the worker
     * @param experimentID the experiment
     * @param platformID the id of the platform
     * @param Worker the worker
     * @return a map where the keys are the detailed population the worker may belong to and the values are the answerOptions
     */
    public Map<PopulationRecord, List<String>> getCalibrations(int experimentID, String platformID, int Worker) {
        List<Integer> answeredCalibrations = DSL.select()
                .from(POPULATIONRESULT)
                .join(POPULATIONANSWEROPTION).onKey()
                .where(POPULATIONRESULT.WORKER.eq(Worker))
                .fetch(POPULATIONANSWEROPTION.POPULATION);

        List<Integer> populationIDs = create.selectFrom(EXPERIMENTSPOPULATION)
                .where(EXPERIMENTSPOPULATION.POPULATION_USER.eq(experimentID))
                .and(EXPERIMENTSPOPULATION.REFERENCED_PLATFORM.eq(platformID))
                .and(EXPERIMENTSPOPULATION.REFERENCED_POPULATION.notIn(answeredCalibrations))
                .fetch(EXPERIMENTSPOPULATION.REFERENCED_POPULATION);

        Map<PopulationRecord, Result<Record>> populationAndAnswers = create.select()
                .from(Tables.POPULATION)
                .join(Tables.POPULATIONANSWEROPTION).onKey()
                .where(Tables.POPULATION.IDPOPULATION.in(populationIDs))
                .fetchGroups(Tables.POPULATION);

        return mapMap(populationAndAnswers, record -> record.getValues(Tables.POPULATIONANSWEROPTION.ANSWER));
    }
}
