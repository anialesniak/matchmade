package parameters;

public interface Scalable
{
    double calculateMatchPercentage(NonScalableFixedParameter parameter);
    void expandBy(double value);
}
