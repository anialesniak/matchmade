package matchmaker;

import clients.Client;

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
    private Set<Client> clientSet;

    ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    public Set<Client> getClients()
    {
        return clientSet;
    }
}
