package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.query.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

/**
 * @author LeanderK
 * @version 1.0
 */
public class RatpackRouter {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    private final MessageRenderer messageRenderer = new MessageRenderer();
    private final Queries queries;
    private final Commands commands;
    private final int port;

    /**
     * creates a new Router.
     * @param queries the query to call
     * @param commands the commands to call
     * @param port the port the server is listening on
     */
    public RatpackRouter(Queries queries, Commands commands, int port) {
        this.queries = queries;
        this.commands = commands;
        this.port = port;
    }

    public void init() throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().port(port))
                .handlers(chain -> chain
                        .register(Registry.single(messageRenderer))
                        .get("/next/:platform/:experiment", ctx -> ctx.render(queries.getNext(ctx)))
                        .get("/preview/:experiment", ctx -> ctx.render(queries.preview(ctx)))
                        .post("/emails/:platform", )
                        .post("/answers/:workerID", )
                        .post("/ratings/:workerID", )
                        .post("/calibrations/:workerID", )
                )
        )
    }
}
