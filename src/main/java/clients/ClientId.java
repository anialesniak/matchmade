package clients;

/**
 * Unique ID for each {@link PoolClient} present in the {@link matchmaker.ClientPool}
 */
public class ClientId
{
    private static long currentId = 0;

    synchronized public static long getNext()
    {
        return currentId++;
    }
}
