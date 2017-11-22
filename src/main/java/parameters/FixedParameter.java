package parameters;

public abstract class FixedParameter
{
    protected final double value;
    protected double expandingRange = 0;

    public FixedParameter(double value)
    {
        this.value = value;
    }

    public void setExpandingRange(double expandingRange) {
        this.expandingRange = expandingRange;
    }

    public double getValue()
    {
        return value;
    }

    public double getExpandingRange() {
        return expandingRange;
    }
}
