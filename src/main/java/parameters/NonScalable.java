package parameters;

public interface NonScalable extends Parameter
{
    boolean matches(FixedParameter value, double epsilon);
}
