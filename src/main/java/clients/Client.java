package clients;

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

        if (clientId != client.clientId) return false;
        if (selfData != null ? !selfData.equals(client.selfData) : client.selfData != null) return false;
        return searchingData != null ? searchingData.equals(client.searchingData) : client.searchingData == null;
    }

    @Override
    public int hashCode()
    {
        int result = clientId;
        result = 31 * result + (selfData != null ? selfData.hashCode() : 0);
        result = 31 * result + (searchingData != null ? searchingData.hashCode() : 0);
        return result;
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
