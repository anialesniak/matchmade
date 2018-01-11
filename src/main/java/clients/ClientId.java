package clients;

/**
 * Unique ID for each {@link PoolClient} present in the {@link matchmaker.ClientPool}
 */
public class ClientId {

    private static int currentID = 0;

    synchronized public static int getNextID(){
        return currentID++;
    }
}
