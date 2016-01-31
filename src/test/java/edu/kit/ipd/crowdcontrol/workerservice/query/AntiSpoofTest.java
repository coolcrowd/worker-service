package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

/**
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoofTest {
    Predicate<String> regex = input -> input.matches(AntiSpoof.REGEX);

    private AntiSpoof antiSpoof;
    private OperationsDataHolder operationsDataHolder;
    private ExperimentRecord experiment;

    @Before
    public void setUp() {
        operationsDataHolder = new OperationsDataHolder();
        experiment = operationsDataHolder.getExperimentRecord();
        antiSpoof = new AntiSpoof(operationsDataHolder.createExperimentOperations(), operationsDataHolder.createTaskOperations());
    }

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
            String random = OperationsDataHolder.nextRandomString();
            if (!random.contains("ab") && !random.contains("pc")) {
                assertTrue(!regex.test(random));
            }
        }
    }

    private Optional<View.Type> testNextHelper(int phase1, int phase2, int givenTotalAnswers, boolean disableAnswer) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(String.valueOf(1), phase1 + "ab");
        map.put(String.valueOf(2), phase2 + "ab");
        if (disableAnswer) {
            operationsDataHolder.setAnswerGiveCountWorker(experiment.getAnwersPerWorker());
        }
        operationsDataHolder.setAnswerCountTotal(givenTotalAnswers);
        operationsDataHolder.setTaskChooserParams(map);
        TaskOperations taskOperations = operationsDataHolder.createTaskOperations();
        ExperimentOperations experimentOperations = operationsDataHolder.createExperimentOperations();
        AntiSpoof antiSpoof = new AntiSpoof(experimentOperations, taskOperations);
        return antiSpoof.next(prepareBuilder(), null, experiment.getIdExperiment(), null, false, false)
                .map(View::getType);
    }

    private View.Builder prepareBuilder() {
        return View.newBuilder()
                .setWorkerId(operationsDataHolder.getWorkerID());
    }
}