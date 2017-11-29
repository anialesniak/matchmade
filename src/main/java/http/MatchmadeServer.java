package http;

import org.eclipse.jetty.server.Server;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MatchmadeServer
{
    private final ClientRequestHandler requestHandler;

    @Inject
    MatchmadeServer(final ClientRequestHandler requestHandler)
    {
        this.requestHandler = requestHandler;
    }

    public void run()
    {
        final Server server = new Server(8080);
        server.setHandler(requestHandler);
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Well.. exception happened. Looks like server could not be started.");
        }
    }
}
