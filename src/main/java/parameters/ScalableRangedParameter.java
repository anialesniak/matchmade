package parameters;

import com.google.common.collect.Range;

public final class ScalableRangedParameter extends RangedParameter implements Scalable
{
    public ScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }

    @Override
    public double calculateMatchPercentage(NonScalableFixedParameter parameter)
    {
        Range<Double> initialRange = Range.closed(getLower(), getUpper());
        Range<Double> expandedRange = Range.closed(getLower() - getExpandingRange(), getUpper() + getExpandingRange());
        if (!expandedRange.contains(parameter.getValue())) return 0;
        else if (initialRange.contains(parameter.getValue())) return 1;
        else if (parameter.getValue() > initialRange.upperEndpoint())
            return 1 - Math.abs(initialRange.upperEndpoint() - parameter.getValue()) / getExpandingRange();
        else return 1 - Math.abs(initialRange.lowerEndpoint() - parameter.getValue()) / getExpandingRange();
    }

    @Override
    public void expandBy(double value) {
        setExpandingRange(value);
    }

    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(lower, upper);
    }
}
