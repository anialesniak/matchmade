package validation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import configuration.ConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONRequestBodyValidator implements RequestBodyValidator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONRequestBodyValidator.class);

    private final JsonSchema requestJsonSchema;

    public JSONRequestBodyValidator(ConfigurationParameters configurationParameters)
    {
        requestJsonSchema = JSONSchemaCreator.createFor(configurationParameters.getParameterNames());
    }

    @Override
    public void validate(String requestBody) throws InvalidRequestBodyException
    {
        try {
            JsonNode json = new ObjectMapper().readTree(requestBody);
            boolean isValid = requestJsonSchema.validate(json).isSuccess();

            if(!isValid)
                throw new InvalidRequestBodyException("JSON does not match expected schema");
        }
        catch (JsonParseException e) {
            throwInvalidRequestBodyExceptionWith("Failed to convert request body to JSON", e);
        }
        catch (IOException e) {
            throwInvalidRequestBodyExceptionWith("Low level IO exception, could not read JSON contents", e);
        }
        catch (ProcessingException e) {
            throwInvalidRequestBodyExceptionWith("Failed to validate request against created schema", e);
        }
    }

    private void throwInvalidRequestBodyExceptionWith(String message, Throwable cause) throws InvalidRequestBodyException
    {
        LOGGER.error(message);
        throw new InvalidRequestBodyException(message, cause);
    }
}
