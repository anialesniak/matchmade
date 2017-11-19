package matchmaker;

import clients.Client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientPool
{
    private static final ClientPool INSTANCE = new ClientPool();

    private final Set<Client> clientSet;

    private ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    public static ClientPool getInstance()
    {
        return INSTANCE;
    }

    public Set<Client> getClientSet()
    {
        return clientSet;
    }
}
