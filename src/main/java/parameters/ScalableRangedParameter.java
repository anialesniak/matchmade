package parameters;

public final class ScalableRangedParameter extends RangedParameter implements Scalable
{
    public ScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }

    @Override
    public float matches(FixedParameter value)
    {
        // TODO
        return 0;
    }

    @Override
    public void expandBy(float value)
    {

    }
}
