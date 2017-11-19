package parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScalableFixedParameter.class, name = "scalableFixed"),
        @JsonSubTypes.Type(value = ScalableRangedParameter.class, name = "scalableRanged"),
        @JsonSubTypes.Type(value = NonScalableFixedParameter.class, name = "nonScalableFixed"),
        @JsonSubTypes.Type(value = NonScalableRangedParameter.class, name = "nonScalableRanged")
})
public interface Parameter
{
    @JsonProperty("type")
    ParameterType getType();
}
