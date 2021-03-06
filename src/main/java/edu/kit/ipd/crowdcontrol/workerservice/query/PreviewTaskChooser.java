package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentsPlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import ratpack.handling.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The PreviewTaskChooser is a dummy-Task chooser, always invoking
 * @author LeanderK
 * @version 1.0
 */
public class PreviewTaskChooser extends TaskChooserAlgorithm {
    static final String NAME = "preview";

    /**
     * creates an new PreviewTaskChooser
     *
     * @param experimentOperations the ExperimentOperations used to communicate with the database.
     * @param experimentsPlatformOperations       the TaskOperations used to communicate with the database
     */
    public PreviewTaskChooser(ExperimentOperations experimentOperations, ExperimentsPlatformOperations experimentsPlatformOperations) {
        super(experimentOperations, experimentsPlatformOperations);
    }

    /**
     * the name used to Identify the Strategy.
     *
     * @return a string describing the strategy
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * the description of the strategy
     *
     * @return a string describing the strategy
     */
    @Override
    public String getDescription() {
        return "";
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

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     *
     * @param builder      the builder to use
     * @param context the Context of the Request
     * @param experimentID the ID of the experiment
     * @param platform     the platform the worker is working on
     * @param skipCreative whether to skip the Creative-Task
     * @param skipRating   whether to skip the Rating-Task
     * @return empty if finished or view
     */
    @Override
    public Optional<View> next(View.Builder builder, Context context, int experimentID, String platform, boolean skipCreative, boolean skipRating) {
        return Optional.of(prepareBuilder(builder, experimentID).setType(View.Type.ANSWER).build());
    }
}
