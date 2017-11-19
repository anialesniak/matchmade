package clients;

import parameters.Parameter;

import java.util.Map;

public class ClientSearchingData
{
    private final Map<String, Parameter> parameters;

    public ClientSearchingData(final Map<String, Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public Map<String, Parameter> getParameters()
    {
        return parameters;
    }
}
