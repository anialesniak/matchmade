package parameters;

public interface Scalable extends Parameter
{
    double calculateMatchPercentage(NonScalableFixedParameter parameter);
    void expandBy(double value);
    ParameterRanges getRanges();
}
