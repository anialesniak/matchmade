package http;

public class EmptyBodyException extends RuntimeException
{
    public EmptyBodyException(final String message)
    {
        super(message);
    }
}
