package edu.ipd.kit.crowdcontrol.workerservice.strategies;

import edu.ipd.kit.crowdcontrol.workerservice.proto.ViewOuterClass;
import spark.Request;
import spark.Response;

import java.util.HashMap;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Strategies {
    private HashMap<String, Strategy> strategies =  new HashMap<>();

    public Strategies() {

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
}
