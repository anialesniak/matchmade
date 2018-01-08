package validation;

import com.fasterxml.jackson.databind.JsonNode;

public interface Validator
{
    boolean isValid(JsonNode json);
}
