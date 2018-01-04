package parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for parameters which are intended to be <i>fixed</i> - concrete value as opposed to
 * {@link RangedParameter} where parameter is by a range of possible values.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FixedParameter
{
    protected final double value;
    protected double expandingRange = 0;
    protected double expandingStep = 0;

    public FixedParameter(double value, double expandingStep)
    {
        this.value = value;
        this.expandingStep = expandingStep;
    }

    public void setExpandingRange(double expandingRange) {
        this.expandingRange = expandingRange;
    }

    public void setExpandingStep(double expandingStep) {
        this.expandingStep = expandingStep;
    }

    public double getValue()
    {
        return value;
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

        final FixedParameter that = (FixedParameter) o;

        if (Double.compare(that.value, value) != 0) return false;
        return Double.compare(that.expandingRange, expandingRange) == 0;
    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(expandingRange);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString()
    {
        return "FixedParameter{" +
                "value=" + value +
                ", \nexpandingRange=" + expandingRange +
                ", \nexpandingStep=" + expandingStep +
                '}';
    }
}
