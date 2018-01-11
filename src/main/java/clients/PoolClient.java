package clients;

import java.util.Objects;

/**
 * This is the pool of all clients that have been registered by {@code Matchmade} server and are waiting to get
 * matched by {@link algorithm.MatchSearchTree}.
 */
public class PoolClient
{
    private final int clientId;
    private final ClientSelfData selfData;
    private final ClientSearchingData prioritizedSearchingData;

    PoolClient(final int clientId,
               final ClientSelfData selfData,
               final ClientSearchingData prioritizedSearchingData)
    {
        this.clientId = clientId;
        this.selfData = selfData;
        this.prioritizedSearchingData = prioritizedSearchingData;
    }

    public static PoolClientBuilder builder()
    {
        return new PoolClientBuilder();
    }

    public ClientSelfData getSelfData()
    {
        return selfData;
    }

    public ClientSearchingData getPrioritizedSearchingData()
    {
        return prioritizedSearchingData;
    }

    public int getClientID()
    {
        return clientId;
    }

    public void expandParameters(){
        prioritizedSearchingData.expandParameters();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final clients.PoolClient poolClient = (clients.PoolClient) o;
        return clientId == poolClient.clientId &&
                Objects.equals(selfData, poolClient.selfData) &&
                Objects.equals(prioritizedSearchingData, poolClient.prioritizedSearchingData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(clientId, selfData, prioritizedSearchingData);
    }

    @Override
    public String toString()
    {
        return "PoolClient{" +
                "clientId=\n\t" + clientId +
                ", selfData=\n\t" + selfData +
                ", prioritizedSearchingData=\n\t" + prioritizedSearchingData +
                '}';
    }
}
