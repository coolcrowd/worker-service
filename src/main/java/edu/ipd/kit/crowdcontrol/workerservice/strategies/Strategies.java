package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;
import spark.Response;

import java.util.HashMap;

/**
 * decides what the worker should be seeing
 * @author LeanderK
 * @version 1.0
 */
public class Strategies {
    private HashMap<String, Strategy> strategies =  new HashMap<>();

    public Strategies() {
        registerStrategy(new AntiSpoof());
    }

    public void registerStrategy(Strategy strategy) {
        strategies.put(strategy.getName(), strategy);
    }

    public ViewOuterClass.View getNext(Request request, Response response) {
        //TODO Error handling
        String platform = request.params("platform");
        Strategy strategy = strategies.get(platform);
        return strategy.next(request, response);
    }

    public ViewOuterClass.View getStart(Request request, Response response) {
        //TODO Error handling
        String platform = request.params("platform");
        switch (platform) {
            case "mturk" : return ViewOuterClass.View.newBuilder()
                    .setType(ViewOuterClass.View.Type.FINISHED)
                    .build();
            case "pybossa" : return constructCalibrations(request, response);

            default: return ViewOuterClass.View.newBuilder()
                    .setType(ViewOuterClass.View.Type.FINISHED)
                    .build();
        }
    }

    public ViewOuterClass.View constructCalibrations(Request request, Response response) {

    }

    public ViewOuterClass.View constructEmailSubmit(Request request, Response response) {
        return ViewOuterClass.View.newBuilder()
                .setType(ViewOuterClass.View.Type.EMAIL)
                .build();
    }
}
