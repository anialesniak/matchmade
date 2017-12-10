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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FixedParameter that = (FixedParameter) o;

        if (Double.compare(that.value, value) != 0) return false;
        return Double.compare(that.expandingRange, expandingRange) == 0;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(expandingRange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "FixedParameter{" +
                "value=" + value +
                ", \nexpandingRange=" + expandingRange +
                '}';
    }
}
