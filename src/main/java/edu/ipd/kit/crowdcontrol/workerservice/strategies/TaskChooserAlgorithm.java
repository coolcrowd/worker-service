package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.BadRequestException;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord;
import edu.ipd.kit.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;

import java.util.Optional;

/**
 * an algorithm to select which task the worker should work on.
 * @author LeanderK
 * @version 1.0
 */
public abstract class TaskChooserAlgorithm {
    protected ExperimentOperations experimentOperations;

    /**
     * the name used to Identify the Strategy.
     * @return a string describing the strategy
     */
    public abstract String getName();

    /**
     * this method gets called when the worker is ready to work on the rating/creative tasks.
     * Which task or whether the worker is already finished decides this method via return type.
     * Empty means the worker is already finished, or a view which specifies what the worker should work on.
     * @param builder the builder to use
     * @param request the request
     * @return empty if finished or view
     */
    public abstract Optional<ViewOuterClass.View> next(ViewOuterClass.View.Builder builder, Request request, int experimentID);

    /**
     * constructs an AnswerView
     * @param builder the builder to use
     * @param experimentID the id of the experiment
     * @return an instance of View with the Type Answer and the information needed for an to display an answer
     * @throws BadRequestException if the experiment was not found
     */
    protected ViewOuterClass.View constructAnswerView(ViewOuterClass.View.Builder builder, int experimentID) throws BadRequestException {
        ExperimentRecord experimentRecord = experimentOperations.getExperiment(experimentID)
                .orElseThrow(() -> new BadRequestException("unable to find Experiment: " + experimentID));
        //TODO: impl
        return builder.setType(ViewOuterClass.View.Type.ANSWER)
                .build();
    }

    /**
     * constructs an RatingView
     * @param builder the builder to use
     * @param experimentID the ID of the experiment
     * @return an instance of View with the Type Rating and the information needed for an to display an rating
     * @throws BadRequestException if the experiment was not found
     */
    protected ViewOuterClass.View constructRatingView(ViewOuterClass.View.Builder builder, int experimentID) throws BadRequestException {
        ExperimentRecord experimentRecord = experimentOperations.getExperiment(experimentID)
                .orElseThrow(() -> new BadRequestException("unable to find Experiment: " + experimentID));
        //TODO: impl
        return builder.setType(ViewOuterClass.View.Type.RATING)
                .build();
    }
}
