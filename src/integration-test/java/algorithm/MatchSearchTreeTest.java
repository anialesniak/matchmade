package algorithm;

import clients.PoolClient;
import clients.TemporaryClient;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import guice.MatchmadeModule;
import http.ClientRequestHandler;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import org.eclipse.jetty.server.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class MatchSearchTreeTest {

    private static PoolClient client0, client1, client2, client3, client4, client5, client6, client7;
    private static MatchSearchTree searchTree;
    private static ClientPool clientPool;
    private static Configuration configuration;

    @BeforeClass
    public static void populateClients() throws Exception{
        Injector injector = Guice.createInjector(new MatchmadeModule());
        searchTree = injector.getInstance(MatchSearchTree.class);
        clientPool = injector.getInstance(ClientPool.class);
        configuration = injector.getInstance(Configuration.class);
        final ClientRequestHandler requestHandler = injector.getInstance(ClientRequestHandler.class);
        Method method = ClientRequestHandler.class.getDeclaredMethod("convertToTemporaryClient", String.class);
        method.setAccessible(true);

        String json = Resources.toString(
                Resources.getResource("clients/client0.json"), StandardCharsets.UTF_8);
        client0 = PoolClient.builder()
                            .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                            .withConfigurationParameters(configuration.getConfigurationParameters())
                            .build();
        json = Resources.toString(
                Resources.getResource("clients/client1.json"), StandardCharsets.UTF_8);
        client1 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters())
                             .build();
        json = Resources.toString(
                Resources.getResource("clients/client2.json"), StandardCharsets.UTF_8);
        client2 =   PoolClient.builder()
                              .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                              .withConfigurationParameters(configuration.getConfigurationParameters())
                              .build();
        json = Resources.toString(
                Resources.getResource("clients/client3.json"), StandardCharsets.UTF_8);
        client3 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters())
                             .build();
        json = Resources.toString(
                Resources.getResource("clients/client4.json"), StandardCharsets.UTF_8);
        client4 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters())
                             .build();
        json = Resources.toString(
                Resources.getResource("clients/client5.json"), StandardCharsets.UTF_8);
        client5 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters()).build();
        json = Resources.toString(
                Resources.getResource("clients/client6.json"), StandardCharsets.UTF_8);
        client6 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters())
                             .build();
        json = Resources.toString(
                Resources.getResource("clients/client7.json"), StandardCharsets.UTF_8);
        client7 =  PoolClient.builder()
                             .withTemporaryClient((TemporaryClient) method.invoke(requestHandler, json))
                             .withConfigurationParameters(configuration.getConfigurationParameters())
                             .build();

    }

    @Before
    public void addClientsToClientPool() throws Exception
    {
        clientPool.getClients().add(client0);
        clientPool.getClients().add(client1);
        clientPool.getClients().add(client2);
        clientPool.getClients().add(client3);
        clientPool.getClients().add(client4);
        clientPool.getClients().add(client5);
        clientPool.getClients().add(client6);
        clientPool.getClients().add(client7);

        searchTree.initializeSearchTree();
        searchTree.fillSearchTree();
    }

    @After
    public void clearClientPool()
    {
        clientPool.getClients().clear();
        searchTree.clearSearchTree();
    }

    @Test
    public void findMatchingSetForClient0() throws Exception
    {
        Set<PoolClient> matchingSetForClient0 = searchTree.findMatchingSetFor(client0);

        assertThat(matchingSetForClient0).hasSize(3);
        assertThat(matchingSetForClient0)
                .containsOnly(client3, client4, client6);

    }

    @Test
    public void findMatchingSetForClient1() throws Exception
    {
        Set<PoolClient> matchingSetForClient1 = searchTree.findMatchingSetFor(client1);

        assertThat(matchingSetForClient1).hasSize(2);
        assertThat(matchingSetForClient1).containsOnly(client4, client5);

    }

    @Test
    public void tryCreatingAMatchFromClient0AndTheirMatches() throws Exception
    {
        searchTree.fillClientsMatches();
        Set<PoolClient> matchingSetForClient0 = searchTree.findMatchingSetFor(client0);

        Set<PoolClient> match = searchTree.tryCreatingAMatchFrom(client0, matchingSetForClient0);

        assertThat(match).isNotEmpty();
        assertThat(match).hasSize(3);
        assertThat(match)
                .containsOnly(client0, client3, client6);
    }

    @Test
    public void tryCreatingAMatchFromClient1AndTheirMatches() throws Exception
    {
        searchTree.fillClientsMatches();
        Set<PoolClient> matchingSetForClient1 = searchTree.findMatchingSetFor(client1);

        Set<PoolClient> match = searchTree.tryCreatingAMatchFrom(client1, matchingSetForClient1);

        assertThat(match).isEmpty();
    }

    @Test
    public void shouldMatchAllClientsInOneIteration() throws Exception
    {
        // given
        int numberOfParameters = 2;
        int teamSize = 2;
        int numberOfTestedClients = 100;
        ClientPool clientPool = new ClientPool();
        Configuration configuration = mock(Configuration.class);
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class,
                                                               withSettings().stubOnly());
        when(configuration.getConfigurationParameters()).thenReturn(configurationParameters);
        when(configurationParameters.getParameterCount()).thenReturn(numberOfParameters);
        when(configurationParameters.getTeamSize()).thenReturn(teamSize);
        when(configurationParameters.getParameterNames()).thenReturn(Arrays.asList("ranking", "score"));
        Map<Integer, Set<PoolClient>> clientMatches = new HashMap<>();
        KDTree searchTree = new KDTree(numberOfParameters);

        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientPool, configuration);

        for(int clientCounter = 0; clientCounter < numberOfTestedClients; clientCounter++) {
            File file = new File(
                    String.format("src/integration-test/resources/all_matching_clients/client%d.json", clientCounter));
            when(request.getContentLength()).thenReturn(1560);
            when(request.getReader()).thenReturn(new BufferedReader(new FileReader(file)));
            clientRequestHandler.handle("", baseRequest, request, response);
        }

        MatchSearchTree matchSearchTree = new MatchSearchTree(
                clientPool,
                configuration,
                clientMatches,
                searchTree
        );

        // when
        assertThat(matchSearchTree.getNumberOfClientsToMatch()).isEqualTo(numberOfTestedClients);
        matchSearchTree.fillSearchTree();
        matchSearchTree.matchIteration();

        // then
        assertThat(matchSearchTree.getNumberOfClientsToMatch()).isEqualTo(0);
        assertThat(clientMatches).hasSize(0);
    }

    @Test
    public void shouldMatchInThreeIterations() throws Exception
    {
        // given
        int numberOfParameters = 2;
        int teamSize = 2;
        int numberOfTestedClients = 101;
        ClientPool clientPool = new ClientPool();
        Configuration configuration = mock(Configuration.class);
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class,
                                                               withSettings().stubOnly());
        when(configuration.getConfigurationParameters()).thenReturn(configurationParameters);
        when(configurationParameters.getParameterCount()).thenReturn(numberOfParameters);
        when(configurationParameters.getTeamSize()).thenReturn(teamSize);
        when(configurationParameters.getParameterNames()).thenReturn(Arrays.asList("ranking", "score"));
        when(configurationParameters.getBaseStepForParameter("ranking")).thenReturn(1.0);
        when(configurationParameters.getBaseStepForParameter("score")).thenReturn(1.0);
        Map<Integer, Set<PoolClient>> clientMatches = new HashMap<>();
        KDTree searchTree = new KDTree(numberOfParameters);

        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientPool, configuration);

        for(int clientCounter = 0; clientCounter < numberOfTestedClients; clientCounter++) {
            File file = new File(
                    String.format("src/integration-test/resources/director/client%d.json", clientCounter));
            when(request.getContentLength()).thenReturn(1560);
            when(request.getReader()).thenReturn(new BufferedReader(new FileReader(file)));
            clientRequestHandler.handle("", baseRequest, request, response);
        }

        MatchSearchTree matchSearchTree = new MatchSearchTree(
                clientPool,
                configuration,
                clientMatches,
                searchTree
        );

        // when
        assertThat(matchSearchTree.getNumberOfClientsToMatch()).isEqualTo(numberOfTestedClients);
        matchSearchTree.fillSearchTree();
        matchSearchTree.matchIteration();
        matchSearchTree.matchIteration();
        matchSearchTree.matchIteration();

        // then
        assertThat(matchSearchTree.getNumberOfClientsToMatch()).isEqualTo(99);
    }
}