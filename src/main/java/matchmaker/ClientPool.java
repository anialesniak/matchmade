package matchmaker;

import clients.ClientSearchingData;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientPool
{
    private static final ClientPool INSTANCE = new ClientPool();

    private final Set<ClientSearchingData> clientSet;

    private ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    public static ClientPool getInstance()
    {
        return INSTANCE;
    }

    public Set<ClientSearchingData> getClientSet()
    {
        return clientSet;
    }
}
