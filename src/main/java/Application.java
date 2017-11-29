import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.MatchmadeModule;
import http.MatchmadeServer;
import matchmaker.Matchmaker;

public class Application
{
    public void run()
    {
        Injector injector = Guice.createInjector(new MatchmadeModule());
        injector.getInstance(MatchmadeServer.class).run();
        injector.getInstance(Matchmaker.class).run();
    }

    public static void main(String[] args)
    {
        new Application().run();
    }
}
