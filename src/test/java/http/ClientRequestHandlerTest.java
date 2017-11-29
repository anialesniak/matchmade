package http;

import clients.Client;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by annterina on 24.11.17.
 */
public class ClientRequestHandlerTest {

    @Test
    public void convertJsonToClient() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler();
        final String json = Resources.toString(
                Resources.getResource("clients/client1.json"), StandardCharsets.UTF_8);

        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToClient", String.class);
        method.setAccessible(true);
        Client client = (Client) method.invoke(clientRequestHandler, json);

        assertThat(client).extracting("clientId").hasOnlyElementsOfType(Integer.class);
        assertThat(client).extracting("selfData").extracting("parameters").isNotEmpty();
        assertThat(client).extracting("searchingData").extracting("parameters").isNotEmpty();
    }


}
