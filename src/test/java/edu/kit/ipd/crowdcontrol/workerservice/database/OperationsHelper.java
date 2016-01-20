package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.query.AntiSpoof;
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
        when(record.getIdExperiment()).thenReturn(experiment);
        return record;
    }

    public ExperimentOperations prepareExperimentOperations(int experiment, ExperimentRecord record, String taskChooser, List<String> constraints) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        ExperimentOperations operations = mock(ExperimentOperations.class);
        when(operations.getExperiment(experiment)).thenReturn(record);
        Result<ConstraintRecord> constraintRecords = create.newResult(Tables.CONSTRAINT);
        if (constraints != null) {
            for (int i = 0; i < constraints.size(); i++) {
                ConstraintRecord constraintRecord = create.newRecord(Tables.CONSTRAINT);
                constraintRecord.setConstraint(constraints.get(i));
                constraintRecord.setExperiment(experiment);
                constraintRecord.setIdConstraint(i);
                constraintRecords.add(constraintRecord);
            }
        }
        when(operations.getConstraints(experiment)).thenReturn(constraintRecords);
        when(operations.insertTaskChooserOrIgnore(taskChooser)).thenReturn(true);
        when(operations.insertTaskChooserOrIgnore(AntiSpoof.NAME)).thenReturn(true);
        return operations;
    }

    public List<AnswerRecord> generateAnswers(int amount, int experiment) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        List<AnswerRecord> answers = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            AnswerRecord answerRecord = create.newRecord(Tables.ANSWER);
            answerRecord.setAnswer("answer"+i);
            answerRecord.setIdAnswer(i);
            answerRecord.setExperiment(experiment);
            answerRecord.setWorkerId(i);
            answers.add(answerRecord);
        }
        return answers;
    }

    public TaskOperations prepareTaskOperations(int experiment, String platform, int worker, int amount, List<AnswerRecord> answerRecords) {
        TaskOperations taskOperations = mock(TaskOperations.class);
        when(taskOperations.prepareRating(worker, experiment,  amount)).thenReturn(answerRecords);
        return taskOperations;
    }

    public PlatformOperations preparePlatformOperations(String platform, boolean NeedsEmail, boolean calibrations) {
        PlatformOperations platformOperations = mock(PlatformOperations.class);
        PlatformRecord platformRecord = mock(PlatformRecord.class);
        when(platformRecord.getNeedsEmail()).thenReturn(NeedsEmail);
        when(platformRecord.getRenderCalibrations()).thenReturn(calibrations);
        when(platformOperations.getPlatform(platform)).thenReturn(platformRecord);
        return platformOperations;
    }

    public Map<PopulationRecord, Result<PopulationAnswerOptionRecord>> generatePopulations(int experiment) {
        Map<PopulationRecord, Result<PopulationAnswerOptionRecord>> map = new HashMap<>();
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        for (int i = 0; i < 5; i++) {
            PopulationRecord populationRecord = create.newRecord(Tables.POPULATION);
            populationRecord.setDescription("description"+i);
            populationRecord.setExperiment(experiment);
            populationRecord.setIdPopulation(i);
            populationRecord.setName("name"+i);
            populationRecord.setProperty("property"+i);
            Result<PopulationAnswerOptionRecord> result = create.newResult(Tables.POPULATION_ANSWER_OPTION);
            for (int j = 0; j < 5; j++) {
                PopulationAnswerOptionRecord record = create.newRecord(Tables.POPULATION_ANSWER_OPTION);
                record.setAnswer("answer"+j);
                record.setIdPopulationAnswerOption(j);
                record.setPopulation(i);
                result.add(record);
            }
            map.put(populationRecord, result);
        }
        return map;
    }

    public PopulationsOperations preparePopulationOperations(int experimentID, String platform, int workerID,
                                                              boolean belongsToWrongPopulation, Map<PopulationRecord, Result<PopulationAnswerOptionRecord>> calibrations) {
        PopulationsOperations populations = mock(PopulationsOperations.class);
        when(populations.belongsToWrongPopulation(experimentID, platform, workerID)).thenReturn(belongsToWrongPopulation);
        when(populations.getCalibrations(experimentID, platform, workerID)).thenReturn(calibrations);
        return populations;
    }
}
