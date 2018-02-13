package matchmaker;

import clients.PoolClient;

import javax.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class holding clients waiting for a match. This is where clients are being picked up from
 * {@link algorithm.MatchSearchTree} to find matches.
 */
@Singleton
public class ClientPool
{
    private final Set<PoolClient> clientSet;

    public ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    ClientPool(final Set<PoolClient> clientSet)
    {
        this.clientSet = clientSet;
    }

    public Set<PoolClient> getClients()
    {
        return clientSet;
    }

    public int getNumberOfClientsInPool()
    {
        return clientSet.size();
    }

    public void expandClientsParameters()
    {
        clientSet.forEach(PoolClient::expandParameters);
    }
}
