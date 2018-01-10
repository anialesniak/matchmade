package http;

import algorithm.MatchSearchTree;
import clients.TemporaryClient;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.MatchmadeModule;
import matchmaker.ClientPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ClientRequestHandlerTest {

    @Mock
    private ClientPool clientPool;

    @Test
    public void convertJsonToClient() throws Exception {
        Injector injector = Guice.createInjector(new MatchmadeModule());

        final ClientRequestHandler clientRequestHandler = injector.getInstance(ClientRequestHandler.class);

        final String json = Resources.toString(
                Resources.getResource("clients/client1.json"), StandardCharsets.UTF_8);

        final Method method = ClientRequestHandler.class.getDeclaredMethod("convertToTemporaryClient", String.class);
        method.setAccessible(true);
        final TemporaryClient temporaryClient = (TemporaryClient) method.invoke(clientRequestHandler, json);

        assertThat(temporaryClient).isNotNull();
        assertThat(temporaryClient.getClientID()).isGreaterThanOrEqualTo(0);
        assertThat(temporaryClient.getSelfData().getParameters())
                .isNotNull()
                .hasSize(3)
                .containsKeys("age", "height", "weight");
        assertThat(temporaryClient.getSearchingData().getParameters())
                .isNotNull()
                .hasSize(3)
                .containsKeys("age", "height", "weight");
    }
}
