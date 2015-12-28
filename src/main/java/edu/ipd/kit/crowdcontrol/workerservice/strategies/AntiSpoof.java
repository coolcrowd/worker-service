package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;

/**
 * @author LeanderK
 * @version 1.0
 */
public class AntiSpoof implements Strategy {
    public static final String NAME = "AntiSpoof";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ViewOuterClass.View next(Request request) {

        return null;
    }
}
