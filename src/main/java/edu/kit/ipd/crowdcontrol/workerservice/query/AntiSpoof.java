package edu.kit.ipd.crowdcontrol.workerservice.query;

import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.proto.View;
import spark.Request;

import java.util.Optional;

/**
 * This is a 3-Phase TaskChooserAlgorithm, in the first phase only creative tasks are given to workers, then the worker
 * get also Rating tasks. In the last phase the workers only work on rating-tasks.
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoof extends TaskChooserAlgorithm {
    public static final String NAME = "AntiSpoof";

    /**
     * creates an new AntiSpoof
     *
     * @param experimentOperations the ExperimentOperations used to communicate with the database.
     * @param taskOperations the TaskOperations used to communicate with the database
     */
    public AntiSpoof(ExperimentOperations experimentOperations, TaskOperations taskOperations) {
        super(experimentOperations, taskOperations);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     *
     * @param builder      the builder to use
     * @param request      the request
     * @param experimentID the ID of the experiment
     * @param platform     the platform the worker is working on
     * @param skipCreative whether to skip the Creative-Task
     * @param skipRating   whether to skip the Rating-Task
     * @return empty if finished or view
     */
    @Override
    public Optional<View> next(View.Builder builder, Request request, int experimentID, String platform,
                               boolean skipCreative, boolean skipRating) {
        return null;
    }
}
