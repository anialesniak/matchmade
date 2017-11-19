package clients;

import parameters.NonScalableFixedParameter;
import parameters.ScalableFixedParameter;

import java.util.Map;

public class ClientSelfData
{
    private final Map<String, ScalableFixedParameter> scalableParameters;
    private final Map<String, NonScalableFixedParameter> nonScalableParameters;

    public ClientSelfData(final Map<String, ScalableFixedParameter> scalableParameters,
                          final Map<String, NonScalableFixedParameter> nonScalableParameters)
    {
        this.scalableParameters = scalableParameters;
        this.nonScalableParameters = nonScalableParameters;
    }

    public Map<String, ScalableFixedParameter> getScalableParameters()
    {
        return scalableParameters;
    }

    public Map<String, NonScalableFixedParameter> getNonScalableParameters()
    {
        return nonScalableParameters;
    }
}