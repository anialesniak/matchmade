package parameters;

public interface Scalable
{
    double calculateMatchPercentage(NonScalableFixedParameter value);
    void expandBy(double value);
}
