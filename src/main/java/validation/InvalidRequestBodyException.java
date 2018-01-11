package validation;

/**
 * Thrown when server receives a request with body that cannot be interpreted and used as data source by {@code
 * Matchmade}.
 */
public class InvalidRequestBodyException extends Exception
{
    public InvalidRequestBodyException(String msg)
    {
        super(msg);
    }

    public InvalidRequestBodyException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
