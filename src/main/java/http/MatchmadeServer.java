package http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Wrapper class for {@code Matchmade} HTTP server. Based on {@code Jetty} http server implementation. Intended to be
 * started by calling {@link MatchmadeServer#run()}. Works on port 8080.
 */
@Singleton
public class MatchmadeServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchmadeServer.class);

    private final ClientRequestHandler requestHandler;
    private Server server;

    @Inject
    MatchmadeServer(final ClientRequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public void run()
    {
        LOGGER.debug("Matchmade http server starting on 8080...");
        server = new Server(8080);
        ContextHandler context = new ContextHandler();
        context.setContextPath("/");
        context.setHandler(requestHandler);
        context.setAllowNullPathInfo(true);
        server.setHandler(context);
        try {
            server.start();
            //server.join();
        } catch (Exception e) {
            LOGGER.error("Well.. exception was thrown. Looks like server could not be started.", e);
        }
    }

    public void stop()
    {
        try {
            server.stop();
        } catch (Exception e) {
            LOGGER.error("Server couldn't be stopped.");
        }
    }
}
