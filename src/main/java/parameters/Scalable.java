package parameters;

/**
 * Scalable Parameter will be expanded by certain value provided Matchmade cannot find a match for client with such
 * parameter for <i>long</i> period of time. Making parameter scalable ensures reducing the time spent on finding a
 * match at the expense of consistency of matched clients. (Matchmade will match this client with others who might be
 * slightly off parameters searched for)
 */
public interface Scalable extends Parameter
{
    double calculateMatchPercentage(NonScalableFixedParameter parameter);
    void expandBy(double value);
}
