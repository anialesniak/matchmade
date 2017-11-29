package matchmaker;

import clients.Client;

import javax.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public final class ClientPool
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
