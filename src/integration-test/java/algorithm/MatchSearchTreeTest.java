package algorithm;

import clients.PoolClient;
import clients.TemporaryClient;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.MatchmadeModule;
import http.ClientRequestHandler;
import matchmaker.ClientPool;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


public class MatchSearchTreeTest {

    private static TemporaryClient client0, client1, client2, client3, client4, client5, client6, client7;
    private static MatchSearchTree searchTree;
    private static ClientPool clientPool;

    @BeforeClass
    public static void populateClients() throws Exception{
        Injector injector = Guice.createInjector(new MatchmadeModule());
        searchTree = injector.getInstance(MatchSearchTree.class);
        clientPool = injector.getInstance(ClientPool.class);

        final ClientRequestHandler requestHandler = injector.getInstance(ClientRequestHandler.class);
        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToClient", String.class);
        method.setAccessible(true);

        String json = Resources.toString(
                Resources.getResource("clients/client0.json"), StandardCharsets.UTF_8);
        client0 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client1.json"), StandardCharsets.UTF_8);
        client1 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client2.json"), StandardCharsets.UTF_8);
        client2 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client3.json"), StandardCharsets.UTF_8);
        client3 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client4.json"), StandardCharsets.UTF_8);
        client4 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client5.json"), StandardCharsets.UTF_8);
        client5 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client6.json"), StandardCharsets.UTF_8);
        client6 = (TemporaryClient) method.invoke(requestHandler, json);
        json = Resources.toString(
                Resources.getResource("clients/client7.json"), StandardCharsets.UTF_8);
        client7 = (TemporaryClient) method.invoke(requestHandler, json);

    }

    @Before
    public void addClientsToClientPool() throws Exception {

        clientPool.getClients().add(new PoolClient(client0));
        clientPool.getClients().add(new PoolClient(client1));
        clientPool.getClients().add(new PoolClient(client2));
        clientPool.getClients().add(new PoolClient(client3));
        clientPool.getClients().add(new PoolClient(client4));
        clientPool.getClients().add(new PoolClient(client5));
        clientPool.getClients().add(new PoolClient(client6));
        clientPool.getClients().add(new PoolClient(client7));

        searchTree.initializeSearchTree();
        searchTree.fillSearchTree();
    }

    @After
    public void clearClientPool() {
        clientPool.getClients().clear();
        searchTree.clearSearchTree();
    }

    @Test
    public void findMatchingSetForClient0() throws Exception {

        Set<PoolClient> matchingSetForClient0 = searchTree.findMatchingSetFor(new PoolClient(client0));

        assertThat(matchingSetForClient0).hasSize(3);
        assertThat(matchingSetForClient0)
                .containsOnly(new PoolClient(client3), new PoolClient(client4), new PoolClient(client6));

    }

    @Test
    public void findMatchingSetForClient1() throws Exception {

        Set<PoolClient> matchingSetForClient1 = searchTree.findMatchingSetFor(new PoolClient(client1));

        assertThat(matchingSetForClient1).hasSize(2);
        assertThat(matchingSetForClient1).containsOnly(new PoolClient(client4), new PoolClient(client5));

    }

    @Test
    public void tryCreatingAMatchFromClient0AndTheirMatches() throws Exception {

        searchTree.fillClientsMatches();
        Set<PoolClient> matchingSetForClient0 = searchTree.findMatchingSetFor(new PoolClient(client0));

        Set<PoolClient> match = searchTree.tryCreatingAMatchFrom(new PoolClient(client0), matchingSetForClient0);

        assertThat(match).isNotEmpty();
        assertThat(match).hasSize(3); //TODO
        assertThat(match)
                .containsOnly(new PoolClient(client0), new PoolClient(client3), new PoolClient(client6));
    }

    @Test
    public void tryCreatingAMatchFromClient1AndTheirMatches() throws Exception {

        searchTree.fillClientsMatches();
        Set<PoolClient> matchingSetForClient1 = searchTree.findMatchingSetFor(new PoolClient(client1));

        Set<PoolClient> match = searchTree.tryCreatingAMatchFrom(new PoolClient(client1), matchingSetForClient1);

        assertThat(match).isEmpty();
    }

}