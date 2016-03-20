package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformModeMode;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public class OperationsDataHolder {
    private int workerID;
    private int workerQuality;
    private final DSLContext create = DSL.using(SQLDialect.MYSQL);
    private final AlgorithmTaskChooserRecord algorithmTaskChooserRecord;
    private final ExperimentRecord experimentRecord;
    private final List<String> constraints;
    private Map<String, String> taskChooserParams;
    private final PlatformRecord platformRecord;
    private final MockProvider mockProvider;
    private Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> calibrations;
    private boolean belongsToWrongPopulation;
    private final ExperimentsPlatformRecord experimentsPlatformRecord;
    private ExperimentsPlatformModeRecord experimentsPlatformModeRecord;
    private int answerCountTotal;
    private int answerGiveCountWorker;
    private int ratingGivenCountWorker;
    private int availableAnswers;
    private List<AnswerRecord> answerRecords;

    private static SecureRandom random = new SecureRandom();

    public static String nextRandomString() {
        return new BigInteger(130, random).toString(32);
    }

    public static int nextRandomInt() {
        return (int) (Integer.MAX_VALUE * (Math.random()));
    }

    public OperationsDataHolder() {
        workerID = nextRandomInt();
        workerQuality = 9;
        algorithmTaskChooserRecord = generateTaskChooserRecord();
        experimentRecord = generateExperimentRecord(algorithmTaskChooserRecord);
        constraints = generateStringList();
        taskChooserParams = generateStringStringMap();
        platformRecord = generatePlatformRecord();
        mockProvider = new MockProvider(this);
        calibrations = generateCalibrations(experimentRecord);
        experimentsPlatformRecord = generateExperimentsTaskRecord(experimentRecord, platformRecord);
        experimentsPlatformModeRecord = generateExperimentsPlatformModeRecord(experimentsPlatformRecord);
        answerCountTotal = experimentRecord.getNeededAnswers() / 2;
        answerGiveCountWorker = experimentRecord.getAnwersPerWorker() / 2;
        ratingGivenCountWorker = experimentRecord.getRatingsPerWorker() / 2;
        availableAnswers = experimentRecord.getRatingsPerWorker() + 1;
        answerRecords = generateAnswers(availableAnswers, experimentRecord);
    }

    private PlatformRecord generatePlatformRecord() {
        String id = nextRandomString();
        String name = nextRandomString();
        return new PlatformRecord(name, id, "", true, true, false, -1);
    }

    private AlgorithmTaskChooserRecord generateTaskChooserRecord() {
        String id = nextRandomString();
        String description = nextRandomString();
        AlgorithmTaskChooserRecord record = create.newRecord(ALGORITHM_TASK_CHOOSER);
        record.setIdTaskChooser(id);
        record.setDescription(description);
        return record;
    }

    private ExperimentRecord generateExperimentRecord(AlgorithmTaskChooserRecord algorithmTaskChooserRecord) {
        int experimentID = nextRandomInt();
        String title = nextRandomString();
        String description = nextRandomString();
        int neededAnswerAmount = Math.abs((int) (100 * (Math.random())) + 40);
        int answersPerWorkerAmount = neededAnswerAmount / 4;
        int ratingsPerWorkerAmount = Math.abs((int) (100 * (Math.random())) + 40);
        int ratingsPerAnswer = Math.abs((int) (100 * (Math.random())) + 40);
        int qualityThreshold = 0;

        return new ExperimentRecord(experimentID, title, description, null, neededAnswerAmount, ratingsPerAnswer,
                answersPerWorkerAmount, ratingsPerWorkerAmount,
                null, algorithmTaskChooserRecord.getIdTaskChooser(), null, null, null, null, null, null, null, qualityThreshold, null);
    }

    private Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> generateCalibrations(ExperimentRecord experiment) {
        Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> map = new HashMap<>();
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        for (int i = 0; i < 5; i++) {
            CalibrationRecord populationRecord = create.newRecord(CALIBRATION);
            populationRecord.setExperiment(experiment.getIdExperiment());
            populationRecord.setIdCalibration(i);
            populationRecord.setName("name" + i);
            populationRecord.setProperty("property" + i);
            Result<CalibrationAnswerOptionRecord> result = create.newResult(CALIBRATION_ANSWER_OPTION);
            for (int j = 0; j < 5; j++) {
                CalibrationAnswerOptionRecord record = create.newRecord(CALIBRATION_ANSWER_OPTION);
                record.setAnswer("answer" + j);
                record.setIdCalibrationAnswerOption(j);
                record.setCalibration(i);
                result.add(record);
            }
            map.put(populationRecord, result);
        }
        return map;
    }

    private List<String> generateStringList() {
        int number = (int) (100 * (Math.random()));
        return Stream.generate(OperationsDataHolder::nextRandomString)
                .limit(number)
                .collect(Collectors.toList());
    }

    private Map<String, String> generateStringStringMap() {
        return generateStringList().stream()
                .collect(Collectors.toMap(Function.identity(), ignored -> OperationsDataHolder.nextRandomString()));
    }

    private ExperimentsPlatformRecord generateExperimentsTaskRecord(ExperimentRecord experimentRecord, PlatformRecord platformRecord) {
        return new ExperimentsPlatformRecord(nextRandomInt(), experimentRecord.getIdExperiment(), platformRecord.getIdPlatform(), null, null);
    }

    private ExperimentsPlatformModeRecord generateExperimentsPlatformModeRecord(ExperimentsPlatformRecord experimentsPlatformRecord) {
        return new ExperimentsPlatformModeRecord(null, experimentsPlatformRecord.getIdexperimentsPlatforms(),
                ExperimentsPlatformModeMode.normal, null);
    }

    public List<AnswerRecord> generateAnswers(int amount, ExperimentRecord experiment) {
        DSLContext create = DSL.using(SQLDialect.MYSQL);
        List<AnswerRecord> answers = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            AnswerRecord answerRecord = create.newRecord(ANSWER);
            answerRecord.setAnswer("answer" + i);
            answerRecord.setIdAnswer(i);
            answerRecord.setExperiment(experiment.getIdExperiment());
            answerRecord.setWorkerId(i);
            answers.add(answerRecord);
        }
        return answers;
    }

    //setters

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    public void setWorkerQuality(int workerQuality) {
        this.workerQuality = workerQuality;
    }

    public void setBelongsToWrongPopulation(boolean belongsToWrongPopulation) {
        this.belongsToWrongPopulation = belongsToWrongPopulation;
    }

    public void setAnswerCountTotal(int answerCountTotal) {
        this.answerCountTotal = answerCountTotal;
    }

    public void setAnswerGiveCountWorker(int answerGiveCountWorker) {
        this.answerGiveCountWorker = answerGiveCountWorker;
    }

    public void setRatingGivenCountWorker(int ratingGivenCountWorker) {
        this.ratingGivenCountWorker = ratingGivenCountWorker;
    }

    public void setAvailableAnswers(int availableAnswers) {
        this.availableAnswers = availableAnswers;
        answerRecords = generateAnswers(availableAnswers, experimentRecord);
    }

    public void setTaskChooserParams(Map<String, String> taskChooserParams) {
        this.taskChooserParams = taskChooserParams;
    }

    public void setCalibrations(Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> calibrations) {
        this.calibrations = calibrations;
    }

    //getters

    public DSLContext getCreate() {
        return create;
    }

    public ExperimentRecord getExperimentRecord() {
        return experimentRecord;
    }

    public AlgorithmTaskChooserRecord getAlgorithmTaskChooserRecord() {
        return algorithmTaskChooserRecord;
    }

    public List<String> getConstraints() {
        return constraints;
    }

    public Map<String, String> getTaskChooserParams() {
        return taskChooserParams;
    }

    public PlatformRecord getPlatformRecord() {
        return platformRecord;
    }

    public int getWorkerID() {
        return workerID;
    }

    public int getWorkerQuality() {
        return workerQuality;
    }

    public boolean belongsToWrongPopulation() {
        return belongsToWrongPopulation;
    }

    public Map<CalibrationRecord, Result<CalibrationAnswerOptionRecord>> getCalibrations() {
        return calibrations;
    }

    public ExperimentsPlatformRecord getExperimentsPlatformRecord() {
        return experimentsPlatformRecord;
    }

    public ExperimentsPlatformModeRecord getExperimentsPlatformModeRecord() {
        return experimentsPlatformModeRecord;
    }

    public int getAnswerCountTotal() {
        return answerCountTotal;
    }

    public int getAnswerGiveCountWorker() {
        return answerGiveCountWorker;
    }

    public int getRatingGivenCountWorker() {
        return ratingGivenCountWorker;
    }

    public int getAvailableAnswers() {
        return availableAnswers;
    }

    public List<AnswerRecord> getAnswerRecords() {
        return answerRecords;
    }

    //creates

    public ExperimentOperations createExperimentOperations() {
        return new ExperimentOperations(mockProvider.getMockCreate(), false);
    }

    public PlatformOperations createPlatformOperations() {
        return new PlatformOperations(mockProvider.getMockCreate(), false);
    }

    public CalibrationsOperations createPopulationsOperations() {
        return new CalibrationsOperations(mockProvider.getMockCreate(), false);
    }

    public ExperimentsPlatformOperations createExperimentsPlatformOperations() {
        return new ExperimentsPlatformOperations(mockProvider.getMockCreate(), false, createExperimentOperations());
    }

    public WorkerOperations createWorkerOperations() {
        return new WorkerOperations(mockProvider.getMockCreate(), false);
    }
}
