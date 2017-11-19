package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
public final class NonScalableFixedParameter extends FixedParameter implements NonScalable
{
    @JsonCreator
    public NonScalableFixedParameter(@JsonProperty("value") double value)
    {
        super(value);
    }

    @Override
    public boolean matches(FixedParameter value, double epsilon)
    {
        // TODO
        return false;
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.NON_SCALABLE_FIXED;
    }
}
