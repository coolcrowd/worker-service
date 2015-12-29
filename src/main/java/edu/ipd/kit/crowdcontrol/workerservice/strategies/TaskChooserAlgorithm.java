package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;

import java.util.Optional;

/**
 * an algorithm to select which task the worker should work on.
 * @author LeanderK
 * @version 1.0
 */
public abstract class TaskChooserAlgorithm {

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

    protected ViewOuterClass.View constructCreativeView(ViewOuterClass.View.Builder builder, int experimentID) {

    }

    protected ViewOuterClass.View constructRatingView(ViewOuterClass.View.Builder builder, int experimentID) {

    }
}
