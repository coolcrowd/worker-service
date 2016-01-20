package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.OperationsHelper;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * @author LeanderK
 * @version 1.0
 */
public class TaskChooserAlgorithmTest {
    private final OperationsHelper operationsHelper = new OperationsHelper();
    private final int experimentID = 1;
    private final int task = 2;
    private final int workerID = 3;
    private final String platform = "a";
    private final int ratingsPerAnswer = 3;
    private final String title = "title";
    private final String mockTaskChooserName = "mockTaskChooser";
    private final String description = "description";
    private final String pictureUrl = "ww.xy.de";
    private final String pictureLUrl = "ww.xyz.de";
    private final String picture = "{!" + pictureUrl + " " + pictureLUrl + "}";
    private final List<String> constraints = Arrays.asList("const1", "const2");

    @Test
    public void testConstructAnswerView() throws Exception {
        View.Builder builder = prepareBuilder();
        TaskChooserAlgorithm taskChooserAlgorithm = prepareTaskChooser(true, new ArrayList<>());
        View view = taskChooserAlgorithm.constructAnswerView(builder, experimentID);
        assertTrue(view.getType().equals(View.Type.ANSWER));
    }

    @Test
    public void testConstructRatingView() throws Exception {
        View.Builder builder = prepareBuilder();
        List<AnswerRecord> answerRecords = operationsHelper.generateAnswers(ratingsPerAnswer, experimentID);
        TaskChooserAlgorithm taskChooserAlgorithm = prepareTaskChooser(true, answerRecords);
        View view = taskChooserAlgorithm.constructRatingView(builder, experimentID);
        assertTrue(view.getType().equals(View.Type.RATING));
        Assert.assertTrue(builder.getAnswersCount() != 0);
        for (View.Answer answer : builder.getAnswersList()) {
            Assert.assertTrue(answerRecords.get(answer.getId()).getAnswer().equals(answer.getAnswer()));
        }
    }

    @Test
    public void testPrepareBuilder() throws Exception {
        View.Builder builder = prepareBuilder();
        TaskChooserAlgorithm taskChooserAlgorithm = prepareTaskChooser(true, new ArrayList<>());
        View view = taskChooserAlgorithm.constructAnswerView(builder, experimentID);
        assertTrue(view.getType().equals(View.Type.ANSWER));
        Assert.assertTrue(view.getTitle().equals(title));
        Assert.assertTrue(view.getDescription().equals(description));
        Assert.assertTrue(view.getPicturesCount() != 0);
        Assert.assertTrue(view.getPictures(0).getUrl().equals(pictureUrl));
        Assert.assertTrue(view.getPictures(0).getUrlLicense().equals(pictureLUrl));
        Assert.assertTrue(view.getConstraintsCount() != 0);
        Assert.assertTrue(view.getConstraints(0).getName().equals(constraints.get(0)));
        Assert.assertTrue(view.getConstraints(1).getName().equals(constraints.get(1)));
    }

    private View.Builder prepareBuilder() {
        return View.newBuilder()
                .setWorkerId(workerID);
    }

    private TaskChooserAlgorithm prepareTaskChooser(boolean creative, List<AnswerRecord> answers) {
        ExperimentRecord experimentRecord = operationsHelper.prepareExperimentRecord(experimentID, ratingsPerAnswer , mockTaskChooserName, title, description+picture);
        ExperimentOperations experimentOperations = operationsHelper.prepareExperimentOperations(experimentID, experimentRecord, mockTaskChooserName, constraints);
        TaskOperations taskOperations = operationsHelper.prepareTaskOperations(experimentID, platform, workerID, ratingsPerAnswer, answers);
        return new MockTaskChooser(mockTaskChooserName, false, creative, experimentOperations, taskOperations);
    }
}