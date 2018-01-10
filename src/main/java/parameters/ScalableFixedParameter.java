package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.Range;

public final class ScalableFixedParameter extends FixedParameter implements Scalable
{
    @JsonCreator
    public ScalableFixedParameter(@JsonProperty("value") double value,
                                  @JsonProperty("priority") double expandingStep)
    {
        super(value, expandingStep);
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.SCALABLE_FIXED;
    }

    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(value-getExpandingRange(), value+getExpandingRange());
    }

    @Override
    public void expand() {
        this.expandingRange += this.expandingStep;
    }
}
