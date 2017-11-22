package parameters;

public abstract class RangedParameter
{
    protected double lower;
    protected double upper;
    protected double expandingRange = 0;

    public RangedParameter(double lower, double upper)
    {
        this.lower = lower;
        this.upper = upper;
    }

    public void setExpandingRange(double expandingRange) {
        this.expandingRange = expandingRange;
    }

    public double getLower()
    {
        return lower;
    }

    public double getUpper()
    {
        return upper;
    }

    public double getExpandingRange() {
        return expandingRange;
    }
}
