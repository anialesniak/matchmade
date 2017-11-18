package parameters;

public final class NonScalableRangedParameter extends RangedParameter implements NonScalable
{
    public NonScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }

    @Override
    public boolean matches(FixedParameter value, double epsilon)
    {
        // TODO
        return false;
    }
}
