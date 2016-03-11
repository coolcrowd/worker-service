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

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author LeanderK
 * @version 1.0
 */
public class TaskChooserAlgorithmTest {
    private final String mockTaskChooserName = "mockTaskChooser";
    private final String mockTaskChooserDescription= "mockTaskChooserDescription";

    TaskChooserAlgorithm taskChooserAlgorithm;
    private OperationsDataHolder data;
    int experimentID;
    int neededRatings;
    int neededAnswers;

    @Before
    public void setUp() {
        data = new OperationsDataHolder();
        taskChooserAlgorithm = prepareTaskChooser(false, true);
        experimentID = data.getExperimentRecord().getIdExperiment();
        int ratingGivenCountWorker = data.getRatingGivenCountWorker();
        int totalRatingsPerWorker = data.getExperimentRecord().getRatingsPerWorker();
        neededRatings = totalRatingsPerWorker - ratingGivenCountWorker;
        int answerGivenCountWorker = data.getAnswerGiveCountWorker();
        int answersTotalPerWorker = data.getExperimentRecord().getAnwersPerWorker();
        neededAnswers = answersTotalPerWorker - answerGivenCountWorker;
        assertTrue(neededAnswers > 0);
        assertTrue(neededRatings > 0);
    }

    @Test
    public void testConstructAnswerView() throws Exception {
        data.setAnswerGiveCountWorker(0);
        View.Builder builder = prepareBuilder();
        int answersTotalPerWorker = data.getExperimentRecord().getAnwersPerWorker();
        View view = taskChooserAlgorithm.constructAnswerView(builder, data.getWorkerID(), experimentID, answersTotalPerWorker);
        assertTrue(view.getType().equals(View.Type.ANSWER));
        assertTrue(view.getAnswerReservationsCount() == answersTotalPerWorker);
    }


    @Test
    public void testConstructRatingView() throws Exception {
        View.Builder builder = prepareBuilder();
        View view = taskChooserAlgorithm.constructRatingView(builder, prepareContext(), experimentID, neededRatings).get();
        assertTrue(view.getType().equals(View.Type.RATING));
        assertTrue(builder.getAnswersToRateCount() != neededRatings);
        for (View.Answer answer : builder.getAnswersToRateList()) {
            assertTrue(data.getAnswerRecords().get(answer.getId()).getAnswer().equals(answer.getAnswer()));
        }
    }


    @Test
    public void testConstructEmptyRatingView() throws Exception {
        View.Builder builder = prepareBuilder();
        data.setAvailableAnswers(0);
        taskChooserAlgorithm = prepareTaskChooser(false, true);
        Optional<View> optional = taskChooserAlgorithm.constructRatingView(builder, prepareContext(), experimentID, neededRatings);
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

        View.Builder builder = prepareBuilder();
        ExperimentRecord experiment = data.getExperimentRecord();
        experiment.setTitle(title);
        experiment.setDescription(resultingDescription);
        ExperimentOperations experimentOperations = data.createExperimentOperations();
        ExperimentsPlatformOperations experimentsPlatformOperations = data.createExperimentsPlatformOperations();
        MockTaskChooser taskChooserAlgorithm = prepareTaskChooser(experimentOperations, experimentsPlatformOperations);
        View view = taskChooserAlgorithm.prepareBuilder(builder, experiment.getIdExperiment()).build();
        assertTrue(view.getTitle().equals(title));
        assertTrue(view.getDescription().equals(description));
        assertTrue(view.getPicturesCount() != 0);
        assertTrue(view.getPictures(0).getUrl().equals(pictureUrl));
        assertTrue(view.getPictures(0).getUrlLicense().equals(pictureLUrl));
        assertTrue(view.getPictures(1).getUrl().equals(pictureUrl));
        assertTrue(view.getConstraintsCount() == data.getConstraints().size());
        Set<String> originalConstraints = new HashSet<>(data.getConstraints());
        for (View.Constraint constraint : view.getConstraintsList()) {
            assertTrue(originalConstraints.contains(constraint.getName()));
        }
    }


    @Test
    public void testConstructViewSkipSkip() throws Exception {
        View.Builder builder = prepareBuilder();
        Optional<View> optional = taskChooserAlgorithm.constructView(builder, prepareContext(), data.getExperimentRecord().getIdExperiment(), true, true);
        assertTrue(!optional.isPresent());
    }


    @Test
    public void testConstructViewReturningAnswer() throws Exception {
        View.Builder builder = prepareBuilder();
        View view = taskChooserAlgorithm.constructView(builder, prepareContext(), data.getExperimentRecord().getIdExperiment(), false, false).get();
        assertTrue(view.getType().equals(View.Type.ANSWER));
        assertTrue(view.getAnswerReservationsCount() == (neededAnswers));
    }

    @Test
    public void testConstructViewReturningRatingEnoughAnswers() throws Exception {
        View.Builder builder = prepareBuilder();
        data.setAnswerGiveCountWorker(data.getExperimentRecord().getAnwersPerWorker());
        MockTaskChooser taskChooserAlgorithm = prepareTaskChooser(data.createExperimentOperations(), data.createExperimentsPlatformOperations());
        View view = taskChooserAlgorithm.constructView(builder, prepareContext(), data.getExperimentRecord().getIdExperiment(), false, false).get();
        assertTrue(view.getType().equals(View.Type.RATING));
    }

    private View.Builder prepareBuilder() {
        return View.newBuilder();
    }

    private Context prepareContext() {
        Context context = mock(Context.class);
        WorkerID workerID = new WorkerID(data.getWorkerID());
        when(context.get(WorkerID.class)).thenReturn(workerID);
        return context;
    }

    private MockTaskChooser prepareTaskChooser(ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        return prepareTaskChooser(false, true, experimentOperations, experimentsPlatformOperations);
    }

    private MockTaskChooser prepareTaskChooser(boolean finish, boolean creative) {
        return prepareTaskChooser(finish, creative, data.createExperimentOperations(), data.createExperimentsPlatformOperations());
    }

    private MockTaskChooser prepareTaskChooser(boolean finish, boolean creative,
                                               ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        return new MockTaskChooser(mockTaskChooserName, mockTaskChooserDescription, finish, creative, experimentOperations,
                experimentsPlatformOperations);
    }
}