package http;

import org.eclipse.jetty.server.Server;

public class MatchmadeServer
{
    public static void run()
    {
        final Server server = new Server(8080);
        server.setHandler(new ClientRequestHandler());
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Well.. exception happened. Looks like server could not be started.");
        }
    }
}
