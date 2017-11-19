package clients;

import parameters.NonScalableFixedParameter;

import java.util.Map;

public class Client
{
    private final ClientSelfData selfData;
    private final ClientSearchingData searchingData;

    public Client(final ClientSelfData selfData, final ClientSearchingData searchingData)
    {
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

}
