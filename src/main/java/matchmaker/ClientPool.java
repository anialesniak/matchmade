package matchmaker;

import clients.Client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientPool
{
    private static ClientPool INSTANCE = createEmptyClientPool();

    private Set<Client> clientSet;

    private static ClientPool createEmptyClientPool()
    {
        return new ClientPool();
    }

    private ClientPool()
    {
        clientSet = ConcurrentHashMap.newKeySet();
    }

    private Set<Client> getClientSet()
    {
        return clientSet;
    }

    public static void add(Client client)
    {
        INSTANCE.getClientSet().add(client);
    }

    public static boolean contains(Client client)
    {
        return INSTANCE.getClientSet().contains(client);
    }

    public static boolean remove(Client client)
    {
        return INSTANCE.getClientSet().remove(client);
    }

    public static void clear()
    {
        INSTANCE = createEmptyClientPool();
    }
}
