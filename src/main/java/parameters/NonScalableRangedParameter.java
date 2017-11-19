package parameters;


import com.google.common.collect.Range;

public final class NonScalableRangedParameter extends RangedParameter implements NonScalable
{
    public NonScalableRangedParameter(double lower, double upper)
    {
        super(lower, upper);
    }
}
