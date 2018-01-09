package validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import configuration.ConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONValidator implements Validator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONValidator.class);

    private final ClientRequestJsonSchema requestSchema;

    public JSONValidator(ConfigurationParameters configurationParameters)
    {
        requestSchema = new ClientRequestJsonSchema(configurationParameters.getParameterNames());
    }

    @Override
    public boolean isValid(JsonNode json)
    {
        try {
            return requestSchema.getSchema().validate(json).isSuccess();
        }
        catch (ProcessingException e) {
            LOGGER.error("Failed to validate request against created schema", e);
            return false;
        }
    }

    JsonNode getSchemaJsonNode()
    {
        return requestSchema.getSchemaAsJson();
    }
}
