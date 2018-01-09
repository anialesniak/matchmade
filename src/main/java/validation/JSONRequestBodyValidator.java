package validation;

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
    public boolean isValid(String requestBody)
    {
        try {
            JsonNode json = new ObjectMapper().readTree(requestBody);
            return requestJsonSchema.validate(json).isSuccess();
        }
        catch (IOException e) {
            LOGGER.error("Failed to convert request body to JSON");
            return false;
        }
        catch (ProcessingException e) {
            LOGGER.error("Failed to validate request against created schema", e);
            return false;
        }
    }
}
