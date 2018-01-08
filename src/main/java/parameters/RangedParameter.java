package parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base class for parameters which are intended to be <i>ranged</i> - range of possible values as opposed to
 * {@link FixedParameter} where parameter is one concrete value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RangedParameter
{
    protected final double lower;
    protected final double upper;
    protected double expandingRange = 0;
    protected double expandingStep = 0;

    public RangedParameter(double lower, double upper, double expandingStep)
    {
        this.lower = lower;
        this.upper = upper;
        this.expandingStep = expandingStep;
    }

    public void setExpandingRange(double expandingRange) {
        this.expandingRange = expandingRange;
    }

    public void setExpandingStep(double expandingStep) {
        this.expandingStep = expandingStep;
    }

    public double getLower()
    {
        return lower;
    }

    public double getUpper()
    {
        return upper;
    }

    public double getExpandingRange() {
        return expandingRange;
    }

    public double getExpandingStep() {
        return expandingStep;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RangedParameter that = (RangedParameter) o;

        if (Double.compare(that.lower, lower) != 0) return false;
        return Double.compare(that.expandingRange, expandingRange) == 0;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lower);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(upper);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(expandingRange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "RangedParameter{" +
                "lower=" + lower +
                ", \nupper=" + upper +
                ", \nexpandingRange=" + expandingRange +
                ", \nexpandingStep=" + expandingStep +
                '}';
    }
}
