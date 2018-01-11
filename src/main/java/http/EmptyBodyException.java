package http;

/**
 * Thrown when server receives empty body HTTP request.
 */
public class EmptyBodyException extends RuntimeException
{
    public EmptyBodyException(final String message)
    {
        super(message);
    }
}
