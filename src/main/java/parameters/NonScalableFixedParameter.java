package parameters;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public final class NonScalableFixedParameter extends FixedParameter implements NonScalable
{
    @JsonCreator
    public NonScalableFixedParameter(@JsonProperty("value") double value)
    {
        super(value, 0);
    }

    @Override
    public ParameterType getType()
    {
        return ParameterType.NON_SCALABLE_FIXED;
    }

    @Override
    public ParameterRanges getRanges() {
        return new ParameterRanges(value-expandingRange, value+expandingRange);
    }

    @Override
    public void expand() {}
}
