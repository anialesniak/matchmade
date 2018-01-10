package validation;

public interface RequestBodyValidator
{
    void validate(String requestBody) throws InvalidRequestBodyException;
}
