package clients;

import com.google.inject.Inject;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import parameters.Parameter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by annterina on 04.01.18.
 */
public class PoolClient {

    private final int clientId;
    private final ClientSelfData selfData;
    private final ClientSearchingData prioritizedSearchingData;

    public PoolClient(final TemporaryClient temporaryClient)
    {
        this.clientId = temporaryClient.getClientID();
        this.selfData = temporaryClient.getSelfData();
        this.prioritizedSearchingData = applyParameterBaseStepsTo(temporaryClient.getSearchingData());
    }

    public ClientSelfData getSelfData()
    {
        return selfData;
    }

    public ClientSearchingData getPrioritizedSearchingData()
    {
        return prioritizedSearchingData;
    }

    public int getClientID() {return clientId;}

    private ClientSearchingData applyParameterBaseStepsTo(ClientSearchingData searchingData) {
        ConfigurationParameters configurationParameters = new Configuration().getConfigurationParameters();
        //TODO should get singleton with guice
        Map<String, Parameter> prioritizedSearchingDataMap = new LinkedHashMap<>();
        for (Map.Entry<String, Parameter> entry : searchingData.getParameters().entrySet()) {
            Parameter parameter = entry.getValue();
            parameter.setExpandingStep(entry.getValue().getExpandingStep()
                    *configurationParameters.getBaseStepForParameter(entry.getKey()));
            prioritizedSearchingDataMap.put(entry.getKey(), parameter);
        }
        return new ClientSearchingData(prioritizedSearchingDataMap);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final clients.PoolClient poolClient = (clients.PoolClient) o;
        return clientId == poolClient.clientId &&
                Objects.equals(selfData, poolClient.selfData) &&
                Objects.equals(prioritizedSearchingData, poolClient.prioritizedSearchingData);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(clientId, selfData, prioritizedSearchingData);
    }

    @Override
    public String toString()
    {
        return "PoolClient{" +
                "clientId=\n\t" + clientId +
                ", selfData=\n\t" + selfData +
                ", prioritizedSearchingData=\n\t" + prioritizedSearchingData +
                '}';
    }
}
