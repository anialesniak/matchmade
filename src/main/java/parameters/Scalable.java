package parameters;

public interface Scalable
{
    float matches(FixedParameter value);
    void expandBy(float value);
}
