package http;

import clients.Client;
import com.google.common.io.Resources;
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
        final ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientPool);
        final String json = Resources.toString(
                Resources.getResource("clients/client1.json"), StandardCharsets.UTF_8);

        final Method method = ClientRequestHandler.class.getDeclaredMethod("convertToClient", String.class);
        method.setAccessible(true);
        final Client client = (Client) method.invoke(clientRequestHandler, json);

        assertThat(client).isNotNull();
        assertThat(client.getClientID()).isGreaterThanOrEqualTo(0);
        assertThat(client.getSelfData().getParameters())
                .isNotNull()
                .hasSize(3)
                .containsKeys("age", "height", "weight");
        assertThat(client.getSearchingData().getParameters())
                .isNotNull()
                .hasSize(3)
                .containsKeys("age", "height", "weight");
    }
}
