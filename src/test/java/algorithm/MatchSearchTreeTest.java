package algorithm;

import clients.Client;
import com.google.common.io.Resources;
import http.ClientRequestHandler;
import matchmaker.ClientPool;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by annterina on 24.11.17.
 */
public class MatchSearchTreeTest {

    @Test
    public void findMatchingSetFor() throws Exception {
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler();
        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToClient", String.class);
        method.setAccessible(true);

        String json = Resources.toString(
                Resources.getResource("Clients/client1.json"), StandardCharsets.UTF_8);
        Client client1 = (Client) method.invoke(clientRequestHandler, json);

        json = Resources.toString(
                Resources.getResource("Clients/client2.json"), StandardCharsets.UTF_8);
        Client client2 = (Client) method.invoke(clientRequestHandler, json);

        json = Resources.toString(
                Resources.getResource("Clients/client3.json"), StandardCharsets.UTF_8);
        Client client3 = (Client) method.invoke(clientRequestHandler, json);

        json = Resources.toString(
                Resources.getResource("Clients/client4.json"), StandardCharsets.UTF_8);
        Client client4 = (Client) method.invoke(clientRequestHandler, json);

        json = Resources.toString(
                Resources.getResource("Clients/client5.json"), StandardCharsets.UTF_8);
        Client client5 = (Client) method.invoke(clientRequestHandler, json);

        ClientPool.add(client1);
        ClientPool.add(client2);
        ClientPool.add(client3);
        ClientPool.add(client4);
        ClientPool.add(client5);

        MatchSearchTree.getInstance().fillSearchTree();
        Set<Client> matchingSetForClient1 = MatchSearchTree.getInstance().findMatchingSetFor(client1);

        //TODO
        //Write assertions

    }

}