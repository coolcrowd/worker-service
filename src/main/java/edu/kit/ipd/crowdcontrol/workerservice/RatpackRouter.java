package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.query.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;
import ratpack.registry.Registry;
import ratpack.registry.RegistryBuilder;
import ratpack.registry.internal.DefaultRegistryBuilder;
import ratpack.render.RendererSupport;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

/**
 * @author LeanderK
 * @version 1.0
 */
public class RatpackRouter {
    private static final Logger logger = LoggerFactory.getLogger(RatpackRouter.class);
    private final MessageRenderer messageRenderer = new MessageRenderer();
    private final ErrorHandler errorHandler = new ErrorHandler();
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
        Registry registry = Registry.builder()
                .add(messageRenderer)
                .add(ServerErrorHandler.class, errorHandler)
                .build();
        RatpackServer.start(server -> server
                .serverConfig(ServerConfig.embedded().port(port))
                .registry(registry)
                .handlers(chain -> chain
                        .all(ctx -> {
                            ctx.getResponse().getHeaders().add("access-control-allow-origin", "*");
                            ctx.getResponse().getHeaders().add("access-control-allow-methods", "GET,PUT,POST,PATCH,DELETE,OPTIONS");
                            ctx.getResponse().getHeaders().add("access-control-allow-credentials", "true");
                            ctx.getResponse().getHeaders().add("access-control-allow-headers", "Authorization,Content-Type");
                            ctx.getResponse().getHeaders().add("access-control-expose-headers", "Link,Location");
                            ctx.getResponse().getHeaders().add("access-control-max-age", "86400");
                            ctx.next();
                        })
                        .get("/next/:platform/:experiment", ctx -> ctx.render(queries.getNext(ctx)))
                        .get("/preview/:experiment", ctx -> ctx.render(queries.preview(ctx)))
                        .post("/emails/:platform", ctx -> ctx.render(commands.submitEmail(ctx)))
                        .post("/answers/:workerID", ctx -> ctx.render(commands.submitAnswer(ctx)))
                        .post("/ratings/:workerID", ctx -> ctx.render(commands.submitRating(ctx)))
                        .post("/calibrations/:workerID", ctx -> ctx.render(commands.submitCalibration(ctx)))
                        .options("/*", ctx -> {
                            ctx.getResponse().status(204);
                            ctx.getResponse().contentType("text/plain");
                            ctx.render("");
                        })
                )
        );
    }
}
