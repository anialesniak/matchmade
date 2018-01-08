package validation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JSONValidator implements Validator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONValidator.class);

    private final JsonSchema requestSchema;

    public JSONValidator(Configuration configuration)
    {
        ConfigurationParameters configurationParameters = configuration.getConfigurationParameters();
        requestSchema = createSchema(configurationParameters.getParameterNames());
    }

    @Override
    public boolean isValid(JsonNode json)
    {
        try {
            return requestSchema.validate(json).isSuccess();
        }
        catch (ProcessingException e) {
            // FIXME is ProcessingException thrown only when schema is incorrectly constructed?
            LOGGER.error("Failed to validate request against created schema");
            return false;
        }
    }

    // TODO implement
    private JsonSchema createSchema(List<String> parameterNames)
    {
        ObjectNode schema = new ObjectMapper().createObjectNode();

        try {
            return JsonSchemaFactory.byDefault().getJsonSchema(schema);
        }
        catch (ProcessingException e) {
            throw new IllegalStateException("Failed to create schema based on configuration", e);
        }
    }
}
