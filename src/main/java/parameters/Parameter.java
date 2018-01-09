package parameters;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base class for all parameter types in {@code Matchmade}. Parameters in matchmade can be:
 *
 * <ul>
 *     <li>{@link FixedParameter}</li>
 *     <li>{@link RangedParameter}</li>
 * </ul>
 *
 * as well as:
 *
 * <ul>
 *     <li>{@link Scalable}</li>
 *     <li>{@link NonScalable}</li>
 * </ul>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScalableFixedParameter.class, name = "scalableFixed"),
        @JsonSubTypes.Type(value = ScalableRangedParameter.class, name = "scalableRanged"),
        @JsonSubTypes.Type(value = NonScalableFixedParameter.class, name = "nonScalableFixed"),
        @JsonSubTypes.Type(value = NonScalableRangedParameter.class, name = "nonScalableRanged")
})
public interface Parameter
{
    ParameterType getType();
    ParameterRanges getRanges();
    void expand();
    void setExpandingStep(double expandingStep);
    double getExpandingStep();

}
