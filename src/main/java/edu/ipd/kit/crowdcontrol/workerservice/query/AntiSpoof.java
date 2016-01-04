package edu.ipd.kit.crowdcontrol.workerservice.query;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;

import java.util.Optional;

/**
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoof extends TaskChooserAlgorithm {
    public static final String NAME = "AntiSpoof";

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
     * @param experimentID
     * @return empty if finished or view
     */
    @Override
    public Optional<ViewOuterClass.View> next(ViewOuterClass.View.Builder builder, Request request, int experimentID) {
        return null;
    }
}
