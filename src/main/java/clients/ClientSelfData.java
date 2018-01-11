package clients;

import parameters.NonScalableFixedParameter;

import java.util.Map;
import java.util.Objects;

/**
 * Data holder for the self data of a client. Self data describes client features/parameters and based on them it
 * will be matched with appropriate other clients.
 */
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
        return "ClientSelfData{" +
                "parameters=\n\t" + parameters +
                '}';
    }
}