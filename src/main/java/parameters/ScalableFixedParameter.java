package parameters;

import com.google.common.collect.Range;

public final class ScalableFixedParameter extends FixedParameter implements Scalable
{
    public ScalableFixedParameter(double value)
    {
        super(value);
    }

    @Override
    public double calculateMatchPercentage(NonScalableFixedParameter value)
    {
        Range<Double> range =
                Range.closed(this.getValue() - this.getExpandingRange(),
                        this.getValue() + this.getExpandingRange());
        if (!range.contains(value.getValue())) return 0;
        if (Math.abs(this.getValue() - value.getValue()) < Constants.EPSILON) return 1;
        return 1 - Math.abs(this.getValue() - value.getValue()) / this.getExpandingRange();
    }

    @Override
    public void expandBy(double value)
    {
        this.setExpandingRange(value);
    }
}
