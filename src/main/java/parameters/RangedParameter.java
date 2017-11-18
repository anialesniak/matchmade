package parameters;

public abstract class RangedParameter
{
    protected double lower;
    protected double upper;

    public RangedParameter(double lower, double upper)
    {
        this.lower = lower;
        this.upper = upper;
    }

    public double getLower()
    {
        return lower;
    }

    public double getUpper()
    {
        return upper;
    }
}
