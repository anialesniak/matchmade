package parameters;

import com.google.common.collect.Range;

public final class ScalableRangedParameter extends RangedParameter implements Scalable
{
    public ScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }

    @Override
    public double calculateMatchPercentage(NonScalableFixedParameter value)
    {
        Range<Double> initialRange =
                Range.open(this.getLower(),
                        this.getUpper());
        Range<Double> expandedRange =
                Range.open(this.getLower() - this.getExpandingRange(),
                        this.getUpper() + this.getExpandingRange());
        if (!expandedRange.contains(value.getValue())) return 0;
        if (initialRange.contains(value.getValue())) return 1;
        if (value.getValue() > initialRange.upperEndpoint())
            return 1 - Math.abs(initialRange.upperEndpoint() - value.getValue()) / this.getExpandingRange();
        return 1 - Math.abs(initialRange.lowerEndpoint() - value.getValue()) / this.getExpandingRange();
    }

    @Override
    public void expandBy(double value)
    {
        this.setExpandingRange(value);
    }
}
