package clients;

import configuration.Configuration;
import configuration.Configuration;
import parameters.Parameter;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class used for conversion from {@link TemporaryClient} to {@link PoolClient}. Such conversion requires
 * {@link TemporaryClient} as well as {@link Configuration}.
 */
public class PoolClientBuilder
{
    private TemporaryClient temporaryClient;
    private Configuration configuration;

    PoolClientBuilder() {}

    public PoolClientBuilder withTemporaryClient(final TemporaryClient temporaryClient)
    {
        this.temporaryClient = temporaryClient;
        return this;
    }

    public PoolClientBuilder withConfiguration(final Configuration configuration)
    {
        this.configuration = configuration;
        return this;
    }

    public PoolClient build()
    {
        checkNotNull(temporaryClient);
        checkNotNull(configuration);

        return new PoolClient(temporaryClient.getClientID(),
                temporaryClient.getSelfData(),
                applyParameterBaseStepsTo(temporaryClient.getSearchingData(), configuration));
    }

    private ClientSearchingData applyParameterBaseStepsTo(final ClientSearchingData searchingData,
                                                          final Configuration configuration)
    {
        Map<String, Parameter> prioritizedSearchingDataMap = new LinkedHashMap<>();
        for (Map.Entry<String, Parameter> entry : searchingData.getParameters().entrySet()) {
            Parameter parameter = entry.getValue();
            parameter.setExpandingStep(
                    parameter.getExpandingStep() *configuration.getBaseStepForParameter(entry.getKey()));
            prioritizedSearchingDataMap.put(entry.getKey(), parameter);
        }
        return new ClientSearchingData(prioritizedSearchingDataMap);
    }
}
