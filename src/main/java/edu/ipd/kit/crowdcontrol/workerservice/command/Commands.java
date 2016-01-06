package edu.ipd.kit.crowdcontrol.workerservice.command;

import edu.ipd.kit.crowdcontrol.workerservice.RequestHelper;
import edu.ipd.kit.crowdcontrol.workerservice.objectservice.Communication;
import spark.Request;
import spark.Response;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Commands implements RequestHelper {
    private final Communication communication;

    public Commands(Communication communication) {
        this.communication = communication;
    }

    public Object submitEmail(Request request, Response response) {
        return null;
    }

    public Object submitCalibration(Request request, Response response) {
        return null;
    }

    public Object submitAnswer(Request request, Response response) {
        return null;
    }

    public Object submitRating(Request request, Response response) {
        return null;
    }
}
