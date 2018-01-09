package matchmaker;

import clients.PoolClient;
import clients.TemporaryClient;

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
    private Set<PoolClient> clientSet;

    ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    public Set<PoolClient> getClients()
    {
        return clientSet;
    }

    public void expandClients(){
        clientSet.forEach(PoolClient::expandParameters);
    }
}
