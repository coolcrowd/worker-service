package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperation;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * contains helper-classes for testing classes with a dependency to a Operations-class
 * @author LeanderK
 * @version 1.0
 */
public class OperationsHelper {
    public ExperimentRecord prepareExperimentRecord(int experiment, int ratingsPerAnswer, String taskChooser, String title, String description) {
        ExperimentRecord record = mock(ExperimentRecord.class);
        when(record.getAlgorithmTaskChooser()).thenReturn(taskChooser);
        when(record.getRatingsPerAnswer()).thenReturn(ratingsPerAnswer);
        when(record.getTitel()).thenReturn(title);
        when(record.getDescription()).thenReturn(description);
        when(record.getIdexperiment()).thenReturn(experiment);
        return record;
    }

    public ExperimentOperations prepareExperimentOperations(int experiment, ExperimentRecord record, List<String> constraints) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        ExperimentOperations operations = mock(ExperimentOperations.class);
        when(operations.getExperiment(experiment)).thenReturn(record);
        Result<ConstraintRecord> constraintRecords = create.newResult(Tables.CONSTRAINT);
        for (int i = 0; i < constraints.size(); i++) {
            ConstraintRecord constraintRecord = create.newRecord(Tables.CONSTRAINT);
            constraintRecord.setConstraint(constraints.get(i));
            constraintRecord.setExperiment(experiment);
            constraintRecord.setIdconstraint(i);
            constraintRecords.add(constraintRecord);
        }
        when(operations.getConstraints(experiment)).thenReturn(constraintRecords);
        return operations;
    }

    public List<AnswerRecord> generateAnswers(int amount, int experiment) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        List<AnswerRecord> answers = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            AnswerRecord answerRecord = create.newRecord(Tables.ANSWER);
            answerRecord.setAnswer("answer"+i);
            answerRecord.setIdanswer(i);
            answerRecord.setTask(experiment);
            answerRecord.setWorkerId(i);
            answers.add(answerRecord);
        }
        return answers;
    }

    public TaskOperation prepareTaskOperations(int experiment, int worker, int amount, List<AnswerRecord> answerRecords) {
        TaskOperation taskOperation = mock(TaskOperation.class);
        when(taskOperation.prepareRating(worker, experiment, amount)).thenReturn(answerRecords);
        return taskOperation;
    }

    public PlatformOperations preparePlatformOperations(String platform, boolean NeedsEmail, boolean calibrations) {
        PlatformOperations platformOperations = mock(PlatformOperations.class);
        PlatformRecord platformRecord = mock(PlatformRecord.class);
        when(platformRecord.getNeedsEmail()).thenReturn(NeedsEmail);
        when(platformRecord.getRenderCalibrations()).thenReturn(calibrations);
        when(platformOperations.getPlatform(platform)).thenReturn(platformRecord);
        return platformOperations;
    }

    public Map<PopulationRecord, Result<PopulationansweroptionRecord>> generatePopulations(int experiment) {
        Map<PopulationRecord, Result<PopulationansweroptionRecord>> map = new HashMap<>();
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        for (int i = 0; i < 5; i++) {
            PopulationRecord populationRecord = create.newRecord(Tables.POPULATION);
            populationRecord.setDescription("description"+i);
            populationRecord.setExperiment(experiment);
            populationRecord.setIdpopulation(i);
            populationRecord.setName("name"+i);
            populationRecord.setProperty("property"+i);
            Result<PopulationansweroptionRecord> result = create.newResult(Tables.POPULATIONANSWEROPTION);
            for (int j = 0; j < 5; j++) {
                PopulationansweroptionRecord record = create.newRecord(Tables.POPULATIONANSWEROPTION);
                record.setAnswer("answer"+j);
                record.setIdpopulationansweroption(j);
                record.setPopulation(i);
                result.add(record);
            }
            map.put(populationRecord, result);
        }
        return map;
    }

    public PopulationsOperations preparePopulationOperations(int experimentID, String platform, int workerID,
                                                              boolean belongsToWrongPopulation, Map<PopulationRecord, Result<PopulationansweroptionRecord>> calibrations) {
        PopulationsOperations populations = mock(PopulationsOperations.class);
        when(populations.belongsToWrongPopulation(experimentID, platform, workerID)).thenReturn(belongsToWrongPopulation);
        when(populations.getCalibrations(experimentID, platform, workerID)).thenReturn(calibrations);
        return populations;
    }
}
