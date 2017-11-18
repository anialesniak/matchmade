package http;

import org.eclipse.jetty.server.Server;

public class MatchmadeServer
{
    public static void run()
    {
        // run http server
        new Server(11111);
    }
}
