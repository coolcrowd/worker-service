package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.OperationsHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoofTest {
    private final OperationsHelper operationsHelper = new OperationsHelper();
    private final ExperimentRecord experiment = operationsHelper.generateExperimentRecord(AntiSpoof.NAME);
    private final ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experiment,
            AntiSpoof.DESCRIPTION, new ArrayList<>(), null);
    private final List<AnswerRecord> answerRecords = operationsHelper.generateAnswers(experiment.getRatingsPerWorker(), experiment.getIdExperiment());
    private final int workerID = (int) (100 * (Math.random()));
    private final int givenAnswers = experiment.getAnwersPerWorker()/4;
    private final int givenRatings = experiment.getRatingsPerWorker()/4;
    private final AntiSpoof antiSpoof = new AntiSpoof(experimentOperations, null);
    Predicate<String> regex = input -> input.matches(AntiSpoof.REGEX);

    @Test
    public void testNextPhase1() throws Exception {
        View.Type type = testNextHelper(experiment.getNeededAnswers(), 0, experiment.getNeededAnswers() / 2, false).get();
        assertTrue(type.equals(View.Type.ANSWER));
    }

    @Test
    public void testNextPhase2() throws Exception {
        View.Type type = testNextHelper(0, experiment.getNeededAnswers(), experiment.getNeededAnswers() / 2, false).get();
        assertTrue(type.equals(View.Type.ANSWER));

        type = testNextHelper(0, experiment.getNeededAnswers(), experiment.getNeededAnswers() / 2, true).get();
        assertTrue(type.equals(View.Type.RATING));
    }

    @Test
    public void testNextPhase3() throws Exception {
        View.Type type = testNextHelper(0, experiment.getNeededAnswers() / 2, experiment.getNeededAnswers(), false).get();
        assertTrue(type.equals(View.Type.RATING));
    }

    @Test
    public void testNextPhase3NeedingAnswers() throws Exception {
        View.Type type = testNextHelper(0, experiment.getNeededAnswers() / 2, experiment.getNeededAnswers()-1, false).get();
        assertTrue(type.equals(View.Type.ANSWER));
    }

    @Test
    public void testParameterValueAbsolute() throws Exception {
        int amount = (int) (100 * (Math.random()));
        String parameter = amount + "ab";
        int result = antiSpoof.getParameterValue(parameter, experiment.getIdExperiment());
        assertTrue(result == amount);
    }

    @Test
    public void testParameterValuePercentage() throws Exception {
        double percentage = Math.round(Math.random() * 100.0) / 100.0;
        int amount = (int) Math.round(100 * percentage);
        String parameter = amount + "pc";
        int result = antiSpoof.getParameterValue(parameter, experiment.getIdExperiment());
        int expected = (int) (percentage * experiment.getNeededAnswers());
        assertTrue(result == expected);
    }

    @Test
    public void testRegexAbsolute() throws Exception {
        for (int i = 0; i < 10; i++) {
            int amount = (int) (1000 * (Math.random()));
            assertTrue(regex.test(amount + "ab"));
        }

        assertTrue(!regex.test("ab"));
    }

    @Test
    public void testRegexPercentage() throws Exception {
        for (int i = 0; i < 10; i++) {
            int amount = (int) (100 * (Math.random()));
            assertTrue(regex.test(amount + "pc"));
        }

        assertTrue(!regex.test("pc"));

        for (int i = 0; i < 10; i++) {
            int amount = (int) (1000 * (Math.random()));
            if (amount > 100) {
                assertTrue(!regex.test(amount + "pc"));
            }
        }
    }

    @Test
    public void testRegexRandom() throws Exception {
        for (int i = 0; i < 100; i++) {
            String random = OperationsHelper.nextRandomString();
            if (!random.contains("ab") && !random.contains("pc")) {
                assertTrue(!regex.test(random));
            }
        }
    }

    private Optional<View.Type> testNextHelper(int phase1, int phase2, int givenTotalAnswers, boolean disableAnswer) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(String.valueOf(1), phase1 + "ab");
        map.put(String.valueOf(2), phase2 + "ab");
        int givenAnswers = this.givenAnswers;
        if (disableAnswer) {
            givenAnswers = experiment.getAnwersPerWorker();
        }
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experiment, workerID, givenTotalAnswers,
                givenAnswers, givenRatings, answerRecords, null, null);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experiment, AntiSpoof.DESCRIPTION,
                new ArrayList<>(), map);
        AntiSpoof antiSpoof = new AntiSpoof(experimentOperations, taskOperations);
        return antiSpoof.next(prepareBuilder(), null, experiment.getIdExperiment(), null, false, false)
                .map(View::getType);
    }

    private View.Builder prepareBuilder() {
        return View.newBuilder()
                .setWorkerId(workerID);
    }
}