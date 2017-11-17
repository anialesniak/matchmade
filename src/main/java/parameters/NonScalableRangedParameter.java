package parameters;

public class NonScalableRangedParameter extends RangedParameter implements NonScalable
{

    public NonScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }

    @Override
    public boolean matches(FixedParameter value)
    {
        // TODO
        return false;
    }
}
