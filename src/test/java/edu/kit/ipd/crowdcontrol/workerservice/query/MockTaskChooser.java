package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentsPlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import ratpack.handling.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * a mock Instance for TaskChooserAlgorithm
 * @author LeanderK
 * @version 1.0
 */
class MockTaskChooser extends TaskChooserAlgorithm {
    private final String name;
    private final String description;
    private final boolean creative;
    private final boolean finish;

    public MockTaskChooser(String name, String description, boolean finish, boolean creative,
                           ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        super(experimentOperations, experimentsPlatformOperations);
        this.name = name;
        this.description = description;
        this.finish = finish;
        this.creative = creative;
    }

    public MockTaskChooser(boolean finish, boolean creative, ExperimentRecord experimentRecord,
                           ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        super(experimentOperations, experimentsPlatformOperations);
        this.name = experimentRecord.getAlgorithmTaskChooser();
        this.description = OperationsDataHolder.nextRandomString();
        this.finish = finish;
        this.creative = creative;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * the description of the strategy
     *
     * @return a string describing the strategy
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * the needed parameter for the Strategy.
     *
     * @return a map with the keys the description and the values the sql-regex describing the allowed values
     */
    @Override
    public List<TaskChooserParameter> getParameters() {
        return new ArrayList<>();
    }

    @Override
    public Optional<View> next(View.Builder builder, Context context, int experimentID, String platform, boolean skipCreative, boolean skipRating) {
        if (finish) {
            return Optional.empty();
        }
        if (creative && !skipCreative) {
            return Optional.of(builder.setType(View.Type.ANSWER).build());
        } else if (!skipRating) {
            return Optional.of(builder.setType(View.Type.RATING).build());
        }
        return Optional.empty();
    }

    /**
     * constructs the an WorkerView depending on the passed parameters and the given answers/ratings of a worker.
     *
     * @param builder      the builder to use
     * @param experimentID the experiment worked on
     * @param skipCreative whether to skip the creative-phase
     * @param skipRating   whether to skip the rating-phase
     * @return an view with the type Answer, Rating or empty
     */
    @Override
    public Optional<View> constructView(View.Builder builder, Context context, int experimentID, boolean skipCreative, boolean skipRating) {
        return super.constructView(builder, context, experimentID, skipCreative, skipRating);
    }
}
