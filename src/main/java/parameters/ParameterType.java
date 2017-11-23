package parameters;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ParameterType
{
    NON_SCALABLE_FIXED("nonScalableFixed"),
    NON_SCALABLE_RANGED("nonScalableRanged"),
    SCALABLE_FIXED("scalableFixed"),
    SCALABLE_RANGED("scalableRanged");

    private final String type;

    ParameterType(final String type)
    {
        this.type = type;
    }

    @JsonValue
    private String getType()
    {
        return type;
    }
}
