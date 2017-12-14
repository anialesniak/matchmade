package clients;

import java.util.Objects;

public class Client
{
    private final int clientId;
    private final ClientSelfData selfData;
    private final ClientSearchingData searchingData;

    public Client(final ClientSelfData selfData, final ClientSearchingData searchingData)
    {
        this.clientId = ClientId.getNextID();
        this.selfData = selfData;
        this.searchingData = searchingData;
    }

    public ClientSelfData getSelfData()
    {
        return selfData;
    }

    public ClientSearchingData getSearchingData()
    {
        return searchingData;
    }

    public int getClientID() {return clientId;}

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Client client = (Client) o;
        return clientId == client.clientId &&
                Objects.equals(selfData, client.selfData) &&
                Objects.equals(searchingData, client.searchingData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(clientId, selfData, searchingData);
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "clientId=\n\t" + clientId +
                ", selfData=\n\t" + selfData +
                ", searchingData=\n\t" + searchingData +
                '}';
    }
}
