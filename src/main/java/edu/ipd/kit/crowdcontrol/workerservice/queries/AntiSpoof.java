package edu.ipd.kit.crowdcontrol.workerservice.queries;

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

    @Override
    public Optional<ViewOuterClass.View> next(ViewOuterClass.View.Builder builder, Request request) {
        return Optional.empty();
    }
}
