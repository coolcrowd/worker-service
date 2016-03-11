package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.WorkerID;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentsPlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Before;
import org.junit.Test;
import ratpack.handling.Context;

import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoofTest {
    Predicate<String> regex = input -> input.matches(AntiSpoof.REGEX);

    private AntiSpoof antiSpoof;
    private OperationsDataHolder data;
    private ExperimentRecord experiment;

    @Before
    public void setUp() {
        data = new OperationsDataHolder();
        experiment = data.getExperimentRecord();
        antiSpoof = new AntiSpoof(data.createExperimentOperations(), data.createExperimentsPlatformOperations());
    }

    @Test
    public void testNextPhase1() throws Exception {
        View.Type type = testNextHelper(experiment.getNeededAnswers(), 0, false).get();
        assertTrue(type.equals(View.Type.ANSWER));

        Optional<View.Type> optional = testNextHelper(experiment.getNeededAnswers(), 0, true);
        assertTrue(!optional.isPresent());
    }

    @Test
    public void testNextPhase2() throws Exception {
        View.Type type = testNextHelper(0, experiment.getNeededAnswers()/ 2, false).get();
        assertTrue(type.equals(View.Type.ANSWER));

        View.Type type2 = testNextHelper(0, experiment.getNeededAnswers()/ 2, true).get();
        assertTrue(type2.equals(View.Type.RATING));
    }

    @Test
    public void testNextPhase3() throws Exception {
        View.Type type = testNextHelper(0, experiment.getNeededAnswers(), false).get();
        assertTrue(type.equals(View.Type.RATING));

        data.setAvailableAnswers(0);
        Optional<View.Type> optional = testNextHelper(0, experiment.getNeededAnswers(), false);
        assertTrue(!optional.isPresent());
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

    private Optional<View.Type> testNextHelper(int phase1, int givenTotalAnswers, boolean disableAnswer) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(String.valueOf(1), phase1 + "ab");
        if (disableAnswer) {
            data.setAnswerGiveCountWorker(experiment.getAnwersPerWorker());
        }
        data.setAnswerCountTotal(givenTotalAnswers);
        data.setTaskChooserParams(map);
        ExperimentsPlatformOperations experimentsPlatformOperations = data.createExperimentsPlatformOperations();
        ExperimentOperations experimentOperations = data.createExperimentOperations();
        AntiSpoof antiSpoof = new AntiSpoof(experimentOperations, experimentsPlatformOperations);
        return antiSpoof.next(View.newBuilder(), prepareContext(), experiment.getIdExperiment(), null, false, false)
                .map(View::getType);
    }

    private Context prepareContext() {
        Context context = mock(Context.class);
        WorkerID workerID = new WorkerID(data.getWorkerID());
        when(context.get(WorkerID.class)).thenReturn(workerID);
        return context;
    }
}