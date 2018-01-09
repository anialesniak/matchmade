package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class NonScalableRangedParameter extends RangedParameter implements NonScalable
{
    @JsonCreator
    public NonScalableRangedParameter(@JsonProperty("lower") double lower,
                                      @JsonProperty("upper") double upper)
    {
        super(lower, upper, 0);
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.NON_SCALABLE_RANGED;
    }

    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(lower, upper);
    }

    @Override
    public void expand() {}

}
