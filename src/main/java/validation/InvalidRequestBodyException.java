package validation;

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
