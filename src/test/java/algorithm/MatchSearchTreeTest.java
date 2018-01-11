package algorithm;

import clients.ClientSearchingData;
import clients.ClientSelfData;
import clients.PoolClient;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import org.assertj.core.internal.Integers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MatchSearchTreeTest
{
    private static final PoolClient DUMMY_POOL_CLIENT = getDummyPoolClient(1);
    private static final Set<PoolClient> DUMMY_POOL_CLIENT_SET = getDummyPoolClientSet(10);
    private Configuration configuration;
    private ClientPool clientPool;
    private ConfigurationParameters configurationParameters;
    private Map<Integer, Set<PoolClient>> clientMatches;
    private KDTree searchTree;

    private MatchSearchTree matchSearchTree;

    @Before
    public void setUp()
    {
        configuration = mock(Configuration.class);
        clientPool = mock(ClientPool.class);
        configurationParameters = mock(ConfigurationParameters.class);
        clientMatches = new HashMap<>();
        searchTree = mock(KDTree.class);
        given(configuration.getConfigurationParameters()).willReturn(configurationParameters);
        matchSearchTree = new MatchSearchTree(
                clientPool,
                configuration,
                clientMatches,
                searchTree
        );
    }

    @Test
    public void shouldPassIterationWithNoClients()
    {
        //given
        //when
        matchSearchTree.matchIteration();
        //then
        verify(clientMatches).clear();
        verify(clientPool).getClients();
        verify(clientPool).expandClientsParameters();
    }

    @Test
    public void shouldAddClientsToTree()
    {
        //given
        given(clientPool.getClients()).willReturn(DUMMY_POOL_CLIENT_SET);
        //when
        matchSearchTree.fillSearchTree();
        //then
        verify(searchTree, times(DUMMY_POOL_CLIENT_SET.size())).insert(any(), any());
    }

    @Test
    public void shouldClearClientMatches()
    {
        //given
        final Map<Integer, Set<PoolClient>> clientMatches = new HashMap<>();
        clientMatches.put(5, ImmutableSet.of());
        final MatchSearchTree matchSearchTree = new MatchSearchTree(
                clientPool,
                configuration,
                clientMatches,
                searchTree
        );
        //when
        matchSearchTree.clearSearchTree();
        //then
        assertThat(clientMatches).isEmpty();
    }

    //TODO fillClientsMatches
    // TODO findMatchingSetFor
    // TODO tryCreatingAMatchFrom

    private static Set<PoolClient> getDummyPoolClientSet(int numberOfClients)
    {
        final Set<PoolClient> set = new HashSet<>();
        for (int i=0; i<numberOfClients; i++) {
            set.add(getDummyPoolClient(i));
        }
        return set;
    }

    private static PoolClient getDummyPoolClient(int clientId)
    {
        final ClientSelfData clientSelfData = mock(ClientSelfData.class, withSettings().stubOnly());
        final ClientSearchingData clientSearchingData = mock(ClientSearchingData.class, withSettings().stubOnly());
        final PoolClient poolClient = mock(PoolClient.class, withSettings().stubOnly());
        given(clientSelfData.getParameters()).willReturn(ImmutableMap.of());
        given(clientSearchingData.getParameters()).willReturn(ImmutableMap.of());
        given(poolClient.getPrioritizedSearchingData()).willReturn(clientSearchingData);
        given(poolClient.getSelfData()).willReturn(clientSelfData);
        given(poolClient.getClientID()).willReturn(clientId);
        return poolClient;
    }
}
