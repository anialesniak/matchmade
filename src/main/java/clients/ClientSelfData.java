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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClientSelfData that = (ClientSelfData) o;

        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;
    }

    @Override
    public int hashCode()
    {
        return parameters != null ? parameters.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "ClientSelfData{" +
                "parameters=\n\t" + parameters +
                '}';
    }
}