package clients;

import configuration.ConfigurationParameters;
import parameters.Parameter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class used for conversion from {@link TemporaryClient} to {@link PoolClient}. Such conversion requires
 * {@link TemporaryClient} as well as {@link ConfigurationParameters}.
 */
public class PoolClientBuilder
{
    TemporaryClient temporaryClient;
    ConfigurationParameters configurationParameters;

    PoolClientBuilder() {}

    public PoolClientBuilder withTemporaryClient(final TemporaryClient temporaryClient)
    {
        this.temporaryClient = temporaryClient;
        return this;
    }

    public PoolClientBuilder withConfigurationParameters(final ConfigurationParameters configurationParameters)
    {
        this.configurationParameters = configurationParameters;
        return this;
    }

    public PoolClient build()
    {
        checkNotNull(temporaryClient);
        checkNotNull(configurationParameters);

        return new PoolClient(temporaryClient.getClientID(),
                temporaryClient.getSelfData(),
                applyParameterBaseStepsTo(temporaryClient.getSearchingData(), configurationParameters));
    }

    private ClientSearchingData applyParameterBaseStepsTo(final ClientSearchingData searchingData,
                                                          final ConfigurationParameters configurationParameters)
    {
        Map<String, Parameter> prioritizedSearchingDataMap = new LinkedHashMap<>();
        for (Map.Entry<String, Parameter> entry : searchingData.getParameters().entrySet()) {
            Parameter parameter = entry.getValue();
            parameter.setExpandingStep(
                    parameter.getExpandingStep() *configurationParameters.getBaseStepForParameter(entry.getKey()));
            prioritizedSearchingDataMap.put(entry.getKey(), parameter);
        }
        return new ClientSearchingData(prioritizedSearchingDataMap);
    }
}
