package http;

/**
 * Thrown when server receives empty body http request.
 */
public class EmptyBodyException extends RuntimeException
{
    public EmptyBodyException(final String message)
    {
        super(message);
    }
}
