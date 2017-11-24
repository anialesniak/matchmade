package parameters;

public final class NonScalableRangedParameter extends RangedParameter implements NonScalable
{
    public NonScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }
    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(lower, upper);
    }
}
