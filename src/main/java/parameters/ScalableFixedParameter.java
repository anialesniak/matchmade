package parameters;

public final class ScalableFixedParameter extends FixedParameter implements Scalable
{
    public ScalableFixedParameter(double value)
    {
        super(value);
    }

    @Override
    public float calculateMatchPercentage(FixedParameter value)
    {
        // TODO
        return 0;
    }

    @Override
    public void expandBy(double value)
    {

    }
}
