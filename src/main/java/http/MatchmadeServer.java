package http;

import org.eclipse.jetty.server.Server;
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

    @Inject
    MatchmadeServer(final ClientRequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public void run()
    {
        LOGGER.info("Matchmade http server starting on 8080...");
        final Server server = new Server(8080);
        server.setHandler(requestHandler);
        try {
            server.start();
        } catch (Exception e) {
            LOGGER.error("Well.. exception was thrown. Looks like server could not be started.", e);
        }
    }
}
