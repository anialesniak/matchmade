package validation;

/**
 * Interface representing any kind of validator that decides if a given HTTP request contains data that can be used
 * by {@code Matchmade} server.
 */
public interface RequestBodyValidator
{
    void validate(String requestBody) throws InvalidRequestBodyException;
}
