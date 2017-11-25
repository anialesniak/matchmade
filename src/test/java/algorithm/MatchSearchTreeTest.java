package algorithm;

import clients.Client;
import com.google.common.io.Resources;
import http.ClientRequestHandler;
import matchmaker.ClientPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


/**
 * Created by annterina on 24.11.17.
 */
public class MatchSearchTreeTest {

    private Client client0, client1, client2, client3, client4, client5, client6, client7;

    @Before
    public void addClientsToClientPool() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler();
        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToClient", String.class);
        method.setAccessible(true);

        String json = Resources.toString(
                Resources.getResource("Clients/client0.json"), StandardCharsets.UTF_8);
        client0 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client1.json"), StandardCharsets.UTF_8);
        client1 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client2.json"), StandardCharsets.UTF_8);
        client2 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client3.json"), StandardCharsets.UTF_8);
        client3 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client4.json"), StandardCharsets.UTF_8);
        client4 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client5.json"), StandardCharsets.UTF_8);
        client5 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client6.json"), StandardCharsets.UTF_8);
        client6 = (Client) method.invoke(clientRequestHandler, json);
        json = Resources.toString(
                Resources.getResource("Clients/client7.json"), StandardCharsets.UTF_8);
        client7 = (Client) method.invoke(clientRequestHandler, json);

        ClientPool.add(client0);
        ClientPool.add(client1);
        ClientPool.add(client2);
        ClientPool.add(client3);
        ClientPool.add(client4);
        ClientPool.add(client5);
        ClientPool.add(client6);
        ClientPool.add(client7);

        MatchSearchTree.getInstance().fillSearchTree();
    }

    @After
    public void clearClientPool() {
        ClientPool.clear();
        MatchSearchTree.getInstance().clearSearchTree();
    }

    @Test
    public void findMatchingSetForClient0() throws Exception {

        Set<Client> matchingSetForClient0 = MatchSearchTree.getInstance().findMatchingSetFor(client0);

        assertThat(matchingSetForClient0).hasSize(3);
        assertThat(matchingSetForClient0).containsOnly(client3, client4, client6);

    }

    @Test
    public void findMatchingSetForClient1() throws Exception {

        Set<Client> matchingSetForClient1 = MatchSearchTree.getInstance().findMatchingSetFor(client1);

        assertThat(matchingSetForClient1).hasSize(2);
        assertThat(matchingSetForClient1).containsOnly(client4, client5);

    }

    @Test
    public void tryCreatingAMatchFromClient0AndTheirMatches() throws Exception {

        MatchSearchTree.getInstance().fillClientsMatches();
        Set<Client> matchingSetForClient0 = MatchSearchTree.getInstance().findMatchingSetFor(client0);

        Set<Client> match = MatchSearchTree.getInstance().tryCreatingAMatchFrom(client0, matchingSetForClient0);

        assertThat(match).isNotEmpty();
        assertThat(match).hasSize(3); //TODO
        assertThat(match).containsOnly(client0, client3, client6);
    }

    @Test
    public void tryCreatingAMatchFromClient1AndTheirMatches() throws Exception {

        MatchSearchTree.getInstance().fillClientsMatches();
        Set<Client> matchingSetForClient1 = MatchSearchTree.getInstance().findMatchingSetFor(client1);

        Set<Client> match = MatchSearchTree.getInstance().tryCreatingAMatchFrom(client1, matchingSetForClient1);

        assertThat(match).isEmpty();
    }

}