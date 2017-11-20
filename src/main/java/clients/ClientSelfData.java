package clients;

import parameters.NonScalableFixedParameter;

import java.util.Map;

public class ClientSelfData
{
    private final Map<String, NonScalableFixedParameter> parameters;

    public ClientSelfData(final Map<String, NonScalableFixedParameter> parameters)
    {
        this.parameters = parameters;
    }

    public Map<String, NonScalableFixedParameter> getParameters()
    {
        return parameters;
    }
}