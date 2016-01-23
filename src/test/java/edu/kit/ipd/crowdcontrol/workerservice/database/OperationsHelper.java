package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.TaskStopgap;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * contains helper-classes for testing classes with a dependency to a Operations-class
 *
 * @author LeanderK
 * @version 1.0
 */
public class OperationsHelper {

    private static SecureRandom random = new SecureRandom();

    public static String nextRandomString() {
        return new BigInteger(130, random).toString(32);
    }

    public ExperimentRecord generateExperimentRecord(String taskChooser) {
        int experimentID = (int) (100 * (Math.random()));
        String title = nextRandomString();
        String description = nextRandomString();
        int answerAmount = (int) (100 * (Math.random())) + 1;
        int neededAnswerAmount = (int) (100 * (Math.random())) + answerAmount + 4;
        int givenAnswerAmount = neededAnswerAmount / 2;
        int answersPerWorkerAmount = givenAnswerAmount / 2;
        int ratingAmount = (int) (100 * (Math.random())) + 1;
        int ratingsPerWorkerAmount = (int) (100 * (Math.random())) + ratingAmount + 1;
        int ratingsPerAnswer = (int) (100 * (Math.random()));

        return new ExperimentRecord(experimentID, title, description, neededAnswerAmount, ratingsPerAnswer,
                answersPerWorkerAmount, ratingsPerWorkerAmount,
                null, taskChooser, null, null, null, null, null, null, null);
    }

    public ExperimentRecord generateExperimentRecord() {
        return generateExperimentRecord(nextRandomString());
    }

    public ExperimentOperations prepareExperimentOperations(int experiment) {
        ExperimentRecord experimentRecord = new ExperimentRecord();
        experimentRecord.setIdExperiment(experiment);
        return prepareExperimentOperations(experimentRecord, null, null, null);
    }

    public ExperimentOperations prepareExperimentOperations(ExperimentRecord record,
                                                            String description, List<String> constraints,
                                                            Map<String, String> param) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        ExperimentOperations operations = mock(ExperimentOperations.class);
        when(operations.getExperiment(record.getIdExperiment())).thenReturn(record);
        Result<ConstraintRecord> constraintRecords = create.newResult(Tables.CONSTRAINT);
        if (constraints != null) {
            for (int i = 0; i < constraints.size(); i++) {
                ConstraintRecord constraintRecord = create.newRecord(Tables.CONSTRAINT);
                constraintRecord.setConstraint(constraints.get(i));
                constraintRecord.setExperiment(record.getIdExperiment());
                constraintRecord.setIdConstraint(i);
                constraintRecords.add(constraintRecord);
            }
        }
        when(operations.getConstraints(record.getIdExperiment())).thenReturn(constraintRecords);
        when(operations.insertTaskChooserOrIgnore(record.getAlgorithmTaskChooser(), description)).thenReturn(true);
        when(operations.getTaskChooserParam(record.getIdExperiment())).thenReturn(param);
        return operations;
    }

    public List<AnswerRecord> generateAnswers(int amount, int experiment) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        List<AnswerRecord> answers = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            AnswerRecord answerRecord = create.newRecord(Tables.ANSWER);
            answerRecord.setAnswer("answer" + i);
            answerRecord.setIdAnswer(i);
            answerRecord.setExperiment(experiment);
            answerRecord.setWorkerId(i);
            answers.add(answerRecord);
        }
        return answers;
    }

    public TaskOperations prepareTaskOperations(ExperimentRecord experimentRecord, int workerID, int givenTotalAnswers,
                                                int givenAnswers, int givenRatings, List<AnswerRecord> answerRecords,
                                                String platform, TaskStopgap taskStopgap) {
        TaskOperations taskOperations = mock(TaskOperations.class);
        when(taskOperations.prepareRating(workerID, experimentRecord.getIdExperiment(), experimentRecord.getRatingsPerWorker() - givenRatings))
                .thenReturn(answerRecords);
        when(taskOperations.getAnswersCount(experimentRecord.getIdExperiment())).thenReturn(givenTotalAnswers);
        when(taskOperations.getAnswersCount(experimentRecord.getIdExperiment(), workerID)).thenReturn(givenAnswers);
        when(taskOperations.getRatingsCount(experimentRecord.getIdExperiment(), workerID)).thenReturn(givenRatings);
        if (taskStopgap == null)
            taskStopgap = TaskStopgap.disabled;
        TaskRecord taskRecord = new TaskRecord(null, experimentRecord.getIdExperiment(), null, null, null, taskStopgap);
        when(taskOperations.getTask(experimentRecord.getIdExperiment(), platform)).thenReturn(taskRecord);
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
            populationRecord.setDescription("description" + i);
            populationRecord.setExperiment(experiment);
            populationRecord.setIdPopulation(i);
            populationRecord.setName("name" + i);
            populationRecord.setProperty("property" + i);
            Result<PopulationAnswerOptionRecord> result = create.newResult(Tables.POPULATION_ANSWER_OPTION);
            for (int j = 0; j < 5; j++) {
                PopulationAnswerOptionRecord record = create.newRecord(Tables.POPULATION_ANSWER_OPTION);
                record.setAnswer("answer" + j);
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
