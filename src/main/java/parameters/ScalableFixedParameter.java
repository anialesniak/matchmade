package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ScalableFixedParameter extends FixedParameter implements Scalable
{
    @JsonCreator
    public ScalableFixedParameter(@JsonProperty("value") double value)
    {
        super(value);
    }

    @Override
    public float calculateMatchPercentage(FixedParameter value)
    {
        // TODO
        return 0;
    }

    @Override
    public void expandBy(double value)
    {

    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.SCALABLE_FIXED;
    }
}
