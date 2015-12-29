package edu.ipd.kit.crowdcontrol.workerservice;

import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import edu.ipd.kit.crowdcontrol.workerservice.queries.Query;
import spark.Request;
import spark.servlet.SparkApplication;

import java.util.function.Function;

import static spark.Spark.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public class Router implements SparkApplication {
    private final Query query;
    JsonFormat protobufJSON = new JsonFormat();

    public Router(Query query) {
        this.query = query;
    }

    /**
     * Invoked from the SparkFilter. Add routes here.
     */
    @Override
    public void init() {
        get("/next/:platform/:experiment", query::getNext);

        put("/email/:worker", );

        put("/answer/:worker", );

        put("/rating/:worker", );

        put("/calibration/:worker", );
    }

    private void get(String route, Function<Request, Message> handler) {
        //TODO: do response things
        spark.Spark.get(route, (request, response) -> protobufJSON.printToString(handler.apply(request)));
    }
}
