package edu.ipd.kit.crowdcontrol.workerservice;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import edu.ipd.kit.crowdcontrol.workerservice.strategies.Strategies;
import spark.Request;
import spark.servlet.SparkApplication;

import java.util.function.Function;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Router implements SparkApplication {
    private final Strategies strategies;
    JsonFormat protobufJSON = new JsonFormat();

    public Router(Strategies strategies) {
        this.strategies = strategies;
    }

    /**
     * Invoked from the SparkFilter. Add routes here.
     */
    @Override
    public void init() {
        get("/next/:experiment", strategies::getNext);

        put("/email")
    }

    private void get(String route, Function<Request, Message> handler) {
        //TODO: do response things
        spark.Spark.get(route, (request, response) -> protobufJSON.printToString(handler.apply(request)));
    }
}
