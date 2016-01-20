package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import spark.Request;

import java.util.Optional;

/**
 * a mock Instance for TaskChooserAlgorithm
 * @author LeanderK
 * @version 1.0
 */
class MockTaskChooser extends TaskChooserAlgorithm {
    private final String name;
    private final boolean creative;
    private final boolean finish;

    public MockTaskChooser(String name, boolean finish, boolean creative, ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        super(experimentOperations, taskOperations);
        this.name = name;
        this.finish = finish;
        this.creative = creative;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<View> next(View.Builder builder, Request request, int experimentID, String platform, boolean skipCreative, boolean skipRating) {
        if (finish) {
            return Optional.empty();
        }
        if (creative && !skipCreative) {
            return Optional.of(constructAnswerView(builder, experimentID));
        } else if (!skipRating) {
            return Optional.of(constructRatingView(builder, experimentID));
        }
        return Optional.empty();
    }
}
