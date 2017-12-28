package parameters;

/**
 * NonScalable Parameter will not change its value throughout whole process of finding a match. Making parameter
 * nonscalable ensures consistency at the expense of duration of finding a match.
 */
public interface NonScalable extends Parameter
{
}
