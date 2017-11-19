package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ScalableRangedParameter extends RangedParameter implements Scalable
{
    @JsonCreator
    public ScalableRangedParameter(@JsonProperty("lower") double lower,
                                   @JsonProperty("upper") double upper)
    {
        super(lower, upper);
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
        return ParameterType.SCALABLE_RANGED;
    }
}
