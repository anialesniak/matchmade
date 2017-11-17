package parameters;

public class NonScalableFixedParameter extends FixedParameter implements NonScalable
{

    public NonScalableFixedParameter(double value)
    {
        super(value);
    }

    @Override
    public boolean matches(FixedParameter value)
    {
        // TODO
        return false;
    }
}
