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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RangedParameter that = (RangedParameter) o;

        if (Double.compare(that.lower, lower) != 0) return false;
        if (Double.compare(that.upper, upper) != 0) return false;
        return Double.compare(that.expandingRange, expandingRange) == 0;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lower);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upper);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(expandingRange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "RangedParameter{" +
                "lower=" + lower +
                ", \nupper=" + upper +
                ", \nexpandingRange=" + expandingRange +
                '}';
    }
}
