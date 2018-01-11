package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.Range;

public final class ScalableRangedParameter extends RangedParameter implements Scalable
{
    @JsonCreator
    public ScalableRangedParameter(@JsonProperty("lower") double lower,
                                   @JsonProperty("upper") double upper,
                                   @JsonProperty("priority") double expandingStep)
    {
        super(lower, upper, expandingStep);
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.SCALABLE_RANGED;
    }

    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(lower-getExpandingRange(), upper+getExpandingRange());
    }

    @Override
    public void expand() {
        this.expandingRange += this.expandingStep;
    }
}
