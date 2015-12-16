package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;
import spark.Response;

/**
 * @author LeanderK
 * @version 1.0
 */
public interface Strategy {
    String getName();
    ViewOuterClass.View next(Request request, Response response);
}
