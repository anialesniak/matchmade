package parameters;

/**
 * Scalable Parameter will be expanded by certain value provided Matchmade cannot find a match for client with such
 * parameter for <i>long</i> period of time. Making parameter scalable ensures reduces the time spend on finding a
 * match by the cost of consistency of searched clients. (Matchmade will match this client with others who might be
 * slightly off parameters searched for)
 */
public interface Scalable extends Parameter
{
    double calculateMatchPercentage(NonScalableFixedParameter parameter);
    void expandBy(double value);
}
