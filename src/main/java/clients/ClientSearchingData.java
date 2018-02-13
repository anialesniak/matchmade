package clients;

import parameters.Parameter;

import java.util.Map;
import java.util.Objects;

/**
 * Data holder for the searching data of a client. Searching data informs about parameter values that a given client
 * expects to find in the self data of matched clients.
 */
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

    public void expandParameters()
    {
        parameters.forEach((key, parameter) -> parameter.expand());
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ClientSearchingData that = (ClientSearchingData) o;
        return Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(parameters);
    }

    @Override
    public String toString()
    {
        return "ClientSearchingData{" +
                "parameters=\n\t" + parameters +
                '}';
    }
}
