package configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationParameters {

    private int parameterCount;
    private int teamSize;
    private Map<String, Double> parameterBaseSteps;

    public int getParameterCount() {
        return parameterCount;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public Map<String, Double> getParameterBaseSteps() {
        return parameterBaseSteps;
    }

    public List<String> getParameterNames()
    {
        return parameterBaseSteps.entrySet()
                                 .stream()
                                 .map(Map.Entry::getKey)
                                 .collect(Collectors.toList());
    }

    public void setParameterCount(int parameterCount) {
        this.parameterCount = parameterCount;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setParameterBaseSteps(Map<String, Double> parameterBaseSteps) {
        this.parameterBaseSteps = parameterBaseSteps;
    }

    public double getBaseStepForParameter(String parameterName) {
        return parameterBaseSteps.get(parameterName);
    }

}
