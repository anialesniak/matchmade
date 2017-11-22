package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.common.collect.Range;

public final class ScalableFixedParameter extends FixedParameter implements Scalable
{
    @JsonCreator
    public ScalableFixedParameter(@JsonProperty("value") double value)
    {
        super(value);
    }

    @Override
    public double calculateMatchPercentage(NonScalableFixedParameter parameter)
    {
        Range<Double> range = Range.closed(getValue() - getExpandingRange(),
                getValue() + getExpandingRange());
        if (!range.contains(parameter.getValue())) return 0;
        else if (Math.abs(getValue() - parameter.getValue()) < Constants.EPSILON) return 1;
        else return 1 - Math.abs(getValue() - parameter.getValue()) / getExpandingRange();
    }

    @Override
    public void expandBy(double value)
    {
        setExpandingRange(value);
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.SCALABLE_FIXED;
    }
}
