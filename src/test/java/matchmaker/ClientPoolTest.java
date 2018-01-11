package matchmaker;

import clients.PoolClient;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientPoolTest {

    private Set<PoolClient> clients;
    private ClientPool clientPool;

    @Before
    public void setUp()
    {
        clients = ImmutableSet.of(
                mock(PoolClient.class),
                mock(PoolClient.class),
                mock(PoolClient.class)
        );
        clientPool = new ClientPool(clients);
    }

    @Test
    public void shouldExpandParametersOfAllClients()
    {
        //given
        //when
        clientPool.expandClientsParameters();
        //then
        clients.forEach(
                poolClient -> verify(poolClient).expandParameters()
        );
    }

    @Test
    public void shouldReturnNumberOfClientsInPool()
    {
        //given
        //when
        int numberOfClientsInPool = clientPool.getNumberOfClientsInPool();
        //then
        assertThat(numberOfClientsInPool).isEqualTo(clients.size());
    }
}
