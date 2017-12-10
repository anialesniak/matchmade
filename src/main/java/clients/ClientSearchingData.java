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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClientSearchingData that = (ClientSearchingData) o;

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
        return "ClientSearchingData{" +
                "parameters=\n\t" + parameters +
                '}';
    }
}
