package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PopulationansweroptionRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Map;

/**
 * PopulationsOperations contains queries which are concerned with populations.
 * @author LeanderK
 * @version 1.0
 */
public class PopulationsOperations extends AbstractOperation {

    /**
     * creates a new instance of PopulationsOperations
     * @param create the context used to communicate with the database
     */
    public PopulationsOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns all the open Calibrations needed from the worker
     * @param experimentID the experiment
     * @param platformID the id of the platform
     * @param worker the worker
     * @return a map where the keys are the detailed population the worker may belong to and the values are the answerOptions
     */
    public Map<PopulationRecord, Result<PopulationansweroptionRecord>> getCalibrations(int experimentID, String platformID, int worker) {
        SelectConditionStep<Record1<Integer>> answered = DSL.select(Tables.POPULATIONRESULT.ANSWER)
                .from(Tables.POPULATIONRESULT)
                .where(Tables.POPULATIONRESULT.WORKER.eq(worker));


        SelectConditionStep<Record1<Integer>> alreadyBelongingPopulations = DSL.select(Tables.POPULATIONANSWEROPTION.POPULATION)
                .from(Tables.POPULATIONANSWEROPTION)
                .join(Tables.POPULATIONRESULT).onKey()
                .where(Tables.POPULATIONRESULT.IDPOPULATIONRESULT.in(answered));

        Map<PopulationRecord, Result<Record>> populationAndAnswers = create.select()
                .from(Tables.POPULATIONANSWEROPTION)
                .join(Tables.POPULATION).onKey()
                .join(Tables.EXPERIMENTSPOPULATION).onKey()
                .where(Tables.EXPERIMENTSPOPULATION.POPULATION_USER.eq(experimentID))
                .and(Tables.EXPERIMENTSPOPULATION.REFERENCED_PLATFORM.eq(platformID))
                .and(Tables.POPULATION.IDPOPULATION.notIn(alreadyBelongingPopulations))
                .fetchGroups(Tables.POPULATION);

        return mapMap(populationAndAnswers, result -> result.into(Tables.POPULATIONANSWEROPTION.asTable()));
    }

    public boolean belongsToWrongPopulation(int experimentID, String platformID, int worker) {
        SelectConditionStep<Record1<Integer>> answered = DSL.select(Tables.POPULATIONRESULT.ANSWER)
                .from(Tables.POPULATIONRESULT)
                .where(Tables.POPULATIONRESULT.WORKER.eq(worker));

        return create.fetchExists(DSL.selectFrom(Tables.EXPERIMENTSPOPULATION)
                .where(Tables.EXPERIMENTSPOPULATION.POPULATION_USER.eq(experimentID))
                .and(Tables.EXPERIMENTSPOPULATION.REFERENCED_PLATFORM.eq(platformID))
                .and(Tables.EXPERIMENTSPOPULATION.NOT.eq(false).and(Tables.EXPERIMENTSPOPULATION.ANSWER.notIn(answered))
                        .or(Tables.EXPERIMENTSPOPULATION.NOT.eq(true).and(Tables.EXPERIMENTSPOPULATION.ANSWER.in(answered)))));
    }
}
