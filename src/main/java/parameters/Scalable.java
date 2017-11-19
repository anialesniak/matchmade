package parameters;

public interface Scalable extends Parameter
{
    float calculateMatchPercentage(FixedParameter value);
    void expandBy(double value);
}
