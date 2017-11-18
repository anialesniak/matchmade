package clients;

import parameters.NonScalable;
import parameters.Scalable;

import java.util.Map;

public class Client
{
    private final Map<String, Scalable> scalableParameters;
    private final Map<String, NonScalable> nonScalableParameters;

    public Client(final Map<String, Scalable> scalableParameters, final Map<String, NonScalable> nonScalableParameters)
    {
        this.scalableParameters = scalableParameters;
        this.nonScalableParameters = nonScalableParameters;
    }

    public Map<String, Scalable> getScalableParameters()
    {
        return scalableParameters;
    }

    public Map<String, NonScalable> getNonScalableParameters()
    {
        return nonScalableParameters;
    }
}
