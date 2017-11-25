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
}
