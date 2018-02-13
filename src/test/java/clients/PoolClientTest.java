package clients;

import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.Configuration;
import guice.MatchmadeModule;
import http.ClientRequestHandler;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

public class PoolClientTest
{

    private static PoolClient client0, client1;
    private static Configuration configuration;

    @BeforeClass
    public static void populateClients() throws Exception
    {
        Injector injector = Guice.createInjector(new MatchmadeModule());
        configuration = injector.getInstance(Configuration.class);

        final ClientRequestHandler requestHandler = injector.getInstance(ClientRequestHandler.class);
        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToTemporaryClient", String.class);
        method.setAccessible(true);

        String json = Resources.toString(
                Resources.getResource("client0.json"), StandardCharsets.UTF_8);
        client0 = PoolClient.builder().withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json)).withConfiguration(configuration).build();
        json = Resources.toString(
                Resources.getResource("client1.json"), StandardCharsets.UTF_8);
        client1 = PoolClient.builder().withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json)).withConfiguration(configuration).build();
    }

    @Test
    public void expandParameters()
    {
        client0.expandParameters();

        assertThat(client0.getPrioritizedSearchingData().getParameters().get("height").getRanges().getLower())
                .isLessThan(170);
        assertThat(client0.getPrioritizedSearchingData().getParameters().get("height").getRanges().getUpper())
                .isGreaterThan(200);
    }

}