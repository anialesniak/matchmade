package clients;

import parameters.Parameter;

import java.util.Map;

public class ClientSelfData
{
    private final Map<String, Parameter> parameters;

    public ClientSelfData(final Map<String, Parameter> parameters)
    {
        this.parameters = parameters;
    }

    public Map<String, Parameter> getParameters()
    {
        return parameters;
    }
}