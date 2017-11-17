package parameters;

public abstract class FixedParameter
{
    protected final double value;

    public FixedParameter(double value)
    {
        this.value = value;
    }

    public double getValue()
    {
        return value;
    }
}
