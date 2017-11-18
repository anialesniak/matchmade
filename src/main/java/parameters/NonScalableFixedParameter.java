package parameters;

public final class NonScalableFixedParameter extends FixedParameter implements NonScalable
{
    public NonScalableFixedParameter(double value)
    {
        super(value);
    }

    @Override
    public boolean matches(FixedParameter value, float epsilon)
    {
        // TODO
        return false;
    }
}
