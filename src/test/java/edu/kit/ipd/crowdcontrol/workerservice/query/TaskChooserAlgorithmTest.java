package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.OperationsHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;

/**
 * @author LeanderK
 * @version 1.0
 */
public class TaskChooserAlgorithmTest {
    private final String mockTaskChooserName = "mockTaskChooser";
    private final String mockTaskChooserDescription= "mockTaskChooserDescription";

    private final OperationsHelper operationsHelper = new OperationsHelper();
    private final ExperimentRecord experiment = operationsHelper.generateExperimentRecord(mockTaskChooserName);
    private final ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experiment, mockTaskChooserDescription, new ArrayList<>(), null);
    private final List<AnswerRecord> answerRecords = operationsHelper.generateAnswers(experiment.getRatingsPerWorker(), experiment.getIdExperiment());
    private final int workerID = (int) (100 * (Math.random()));
    private final int givenAnswers = experiment.getAnwersPerWorker()/4;
    private final int neededAnswersFromWorker = experiment.getAnwersPerWorker() - givenAnswers;
    private final int givenTotalRatings = experiment.getRatingsPerWorker()/2;
    private final int givenRatings = experiment.getRatingsPerWorker()/4;
    private final int neededRatings = experiment.getRatingsPerWorker() - givenRatings;
    private final TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experiment, workerID, experiment.getNeededAnswers()/2,
            givenAnswers, givenRatings, answerRecords, null, null);
    TaskChooserAlgorithm taskChooserAlgorithm = prepareTaskChooser(false, true, neededAnswersFromWorker, neededRatings);

    @Test
    public void testConstructAnswerView() throws Exception {
        View.Builder builder = prepareBuilder();
        View view = taskChooserAlgorithm.constructAnswerView(builder, experiment.getIdExperiment(), neededAnswersFromWorker);
        assertTrue(view.getType().equals(View.Type.ANSWER));
        assertTrue(view.getMaxAnswersToGive() == neededAnswersFromWorker);
    }

    @Test
    public void testConstructRatingView() throws Exception {
        View.Builder builder = prepareBuilder();
        View view = taskChooserAlgorithm.constructRatingView(builder, experiment.getIdExperiment(), neededRatings).get();
        assertTrue(view.getType().equals(View.Type.RATING));
        assertTrue(builder.getAnswersToRateCount() != 0);
        for (View.Answer answer : builder.getAnswersToRateList()) {
            assertTrue(answerRecords.get(answer.getId()).getAnswer().equals(answer.getAnswer()));
        }
    }

    @Test
    public void testConstructEmptyRatingView() throws Exception {
        View.Builder builder = prepareBuilder();
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experiment, workerID, givenTotalRatings,
                givenAnswers, givenRatings, new ArrayList<>(), null, null);
        MockTaskChooser taskChooserAlgorithm = prepareTaskChooser(experimentOperations, taskOperations);
        Optional<View> optional = taskChooserAlgorithm.constructRatingView(builder, experiment.getIdExperiment(), neededRatings);
        assertTrue(!optional.isPresent());
    }

    @Test
    public void testPrepareBuilder() throws Exception {
        String title = "title";
        String description = "description";
        String pictureUrl = "ww.xy.de";
        String pictureLUrl = "ww.xyz.de";
        String pictures = "{!" + pictureUrl + " " + pictureLUrl + "} {!" + pictureUrl + "}";
        String resultingDescription = description+ pictures;
        List<String> constraints = Arrays.asList("const1", "const2");

        View.Builder builder = prepareBuilder();
        ExperimentRecord experiment = this.experiment.copy();
        experiment.setIdExperiment(this.experiment.getIdExperiment());
        experiment.setTitle(title);
        experiment.setDescription(resultingDescription);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experiment,
                mockTaskChooserDescription, constraints, null);
        MockTaskChooser taskChooserAlgorithm = prepareTaskChooser(experimentOperations, taskOperations);
        View view = taskChooserAlgorithm.prepareBuilder(builder, experiment.getIdExperiment()).build();
        assertTrue(view.getTitle().equals(title));
        assertTrue(view.getDescription().equals(description));
        assertTrue(view.getPicturesCount() != 0);
        assertTrue(view.getPictures(0).getUrl().equals(pictureUrl));
        assertTrue(view.getPictures(0).getUrlLicense().equals(pictureLUrl));
        assertTrue(view.getPictures(1).getUrl().equals(pictureUrl));
        assertTrue(view.getConstraintsCount() != 0);
        assertTrue(view.getConstraints(0).getName().equals(constraints.get(0)));
        assertTrue(view.getConstraints(1).getName().equals(constraints.get(1)));
    }

    @Test
    public void testConstructViewSkipSkip() throws Exception {
        View.Builder builder = prepareBuilder();
        Optional<View> optional = taskChooserAlgorithm.constructView(builder, experiment.getIdExperiment(), true, true);
        assertTrue(!optional.isPresent());
    }

    @Test
    public void testConstructViewReturningAnswer() throws Exception {
        View.Builder builder = prepareBuilder();
        View view = taskChooserAlgorithm.constructView(builder, experiment.getIdExperiment(), false, false).get();
        assertTrue(view.getType().equals(View.Type.ANSWER));
        assertTrue(view.getMaxAnswersToGive() == (experiment.getAnwersPerWorker() - givenAnswers));
    }

    @Test
    public void testConstructViewReturningRatingEnoughAnswers() throws Exception {
        View.Builder builder = prepareBuilder();
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experiment, workerID, givenTotalRatings,
                experiment.getAnwersPerWorker(), givenRatings, answerRecords, null, null);
        MockTaskChooser taskChooserAlgorithm = prepareTaskChooser(experimentOperations, taskOperations);
        View view = taskChooserAlgorithm.constructView(builder, experiment.getIdExperiment(), false, false).get();
        assertTrue(view.getType().equals(View.Type.RATING));
    }

    private View.Builder prepareBuilder() {
        return View.newBuilder()
                .setWorkerId(workerID);
    }

    private MockTaskChooser prepareTaskChooser(ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        return prepareTaskChooser(false, true, 0, 0, experimentOperations, taskOperations);
    }

    private MockTaskChooser prepareTaskChooser(boolean finish, boolean creative, int requestAnswersAmount, int requestRatingAmount) {
        return prepareTaskChooser(finish, creative, requestAnswersAmount, requestRatingAmount, experimentOperations, taskOperations);
    }

    private MockTaskChooser prepareTaskChooser(boolean finish, boolean creative, int requestAnswersAmount, int requestRatingAmount,
                                               ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        return new MockTaskChooser(mockTaskChooserName, mockTaskChooserDescription, finish, creative, experimentOperations,
                taskOperations, requestAnswersAmount, requestRatingAmount);
    }
}