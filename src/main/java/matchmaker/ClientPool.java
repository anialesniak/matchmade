package matchmaker;

import clients.Client;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientPool
{
    private static ClientPool instance = createEmptyClientPool();

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
        instance.getClientSet().add(client);
    }

    public static boolean contains(Client client)
    {
        return instance.getClientSet().contains(client);
    }

    public static boolean remove(Client client)
    {
        return instance.getClientSet().remove(client);
    }

    public static boolean removeAll(Set<Client> clients) {
        return instance.getClientSet().removeAll(clients);
    }

    public static void clear()
    {
        instance = createEmptyClientPool();
    }

    public static Set<Client> getSet(){return instance.clientSet;}
}
