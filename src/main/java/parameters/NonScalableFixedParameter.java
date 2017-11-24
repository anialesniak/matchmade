package parameters;

public final class NonScalableFixedParameter extends FixedParameter implements NonScalable
{
    public NonScalableFixedParameter(double value)
    {
        super(value);
    }
    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(value-expandingRange, value+expandingRange);
    }
}
