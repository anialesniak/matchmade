package validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.util.Arrays;
import java.util.List;

public class JSONSchemaCreator
{
    private static ObjectMapper mapper;
    private static List<String> parameterNames;

    public static JsonSchema createFor(List<String> configurationParameterNames)
    {
        mapper = new ObjectMapper();
        parameterNames = configurationParameterNames;
        return createSchema(createSchemaJsonNode());
    }

    private static JsonNode createSchemaJsonNode()
    {
        ObjectNode schema = createSchemaSpecification();
        schema.set("properties", createSchemaProperties());
        schema.put("additionalProperties", false);
        return schema;
    }

    private static JsonSchema createSchema(JsonNode jsonNode)
    {
        try {
            return JsonSchemaFactory.byDefault().getJsonSchema(jsonNode);
        }
        catch (ProcessingException e) {
            throw new IllegalStateException("Failed to create schema based on configuration", e);
        }
    }

    private static ObjectNode createSchemaSpecification()
    {
        ObjectNode schema = mapper.createObjectNode();
        schema.put("type", "object");

        ArrayNode schemaRequired = mapper.createArrayNode();
        schemaRequired.add("clientSelf");
        schemaRequired.add("clientSearching");
        schema.set("required", schemaRequired);

        return schema;
    }

    private static ObjectNode createSchemaProperties()
    {
        ObjectNode schemaProperties = mapper.createObjectNode();
        schemaProperties.set("clientSelf", createClientSelfSchema());
        schemaProperties.set("clientSearching", createClientSearchingSchema());

        return schemaProperties;
    }

    private static ObjectNode createClientSelfSchema()
    {
        ObjectNode clientSelf = mapper.createObjectNode();
        clientSelf.put("type", "object");
        ArrayNode clientSelfRequired = mapper.createArrayNode();
        parameterNames.forEach(clientSelfRequired::add);
        clientSelf.set("required", clientSelfRequired);
        clientSelf.set("properties", createClientSelfProperties());
        clientSelf.put("additionalProperties", false);

        return clientSelf;
    }

    private static ObjectNode createClientSelfProperties()
    {
        ObjectNode clientSelfProperties = mapper.createObjectNode();
        parameterNames.forEach(propertyName -> clientSelfProperties.set(propertyName, createClientSelfProperty()));

        return clientSelfProperties;
    }

    private static ObjectNode createClientSelfProperty()
    {
        ObjectNode property = mapper.createObjectNode();
        property.put("type", "object");

        ObjectNode properties = mapper.createObjectNode();
        properties.set("type", createStringTypeFieldWithValuePattern("^nonScalableFixed$"));
        properties.set("value", createNumberTypeField());

        property.set("properties", properties);
        property.put("additionalProperties", false);
        return property;
    }

    private static ObjectNode createStringTypeFieldWithValuePattern(String valuePattern)
    {
        ObjectNode stringTypeField = mapper.createObjectNode();
        stringTypeField.put("type", "string");
        stringTypeField.put("pattern", valuePattern);

        return stringTypeField;
    }

    private static ObjectNode createNumberTypeField()
    {
        ObjectNode numberTypeField = mapper.createObjectNode();
        numberTypeField.put("type", "number");
        return numberTypeField;
    }

    private static ObjectNode createClientSearchingSchema()
    {
        ObjectNode clientSearching = mapper.createObjectNode();
        clientSearching.put("type", "object");
        ArrayNode clientSearchingRequired = mapper.createArrayNode();
        parameterNames.forEach(clientSearchingRequired::add);
        clientSearching.set("required", clientSearchingRequired);
        clientSearching.set("properties", createClientSearchingProperties());
        clientSearching.put("additionalProperties", false);

        return clientSearching;
    }

    private static ObjectNode createClientSearchingProperties()
    {
        ObjectNode clientSearchingProperties = mapper.createObjectNode();
        parameterNames.forEach(propertyName -> clientSearchingProperties.set(propertyName,
                                                                             createClientSearchingProperty()));
        return clientSearchingProperties;
    }

    private static ObjectNode createClientSearchingProperty()
    {
        ObjectNode property = mapper.createObjectNode();
        ArrayNode possibleProperties = mapper.createArrayNode();

        possibleProperties.add(createNonScalableFixedJsonObject());
        possibleProperties.add(createScalableFixedJsonObject());
        possibleProperties.add(createNonScalableRangedJsonObject());
        possibleProperties.add(createScalableRangedJsonObject());

        property.set("anyOf", possibleProperties);
        return property;
    }

    private static ObjectNode createNonScalableFixedJsonObject()
    {
        ObjectNode nonScalableFixedProperty = mapper.createObjectNode();
        nonScalableFixedProperty.put("type", "object");

        ArrayNode required = mapper.createArrayNode();
        Arrays.asList("type", "value").forEach(required::add);
        nonScalableFixedProperty.set("required", required);

        ObjectNode nonScalableFixedProperties = mapper.createObjectNode();
        nonScalableFixedProperties.set("type", createStringTypeFieldWithValuePattern("^nonScalableFixed$"));
        nonScalableFixedProperties.set("value", createNumberTypeField());

        nonScalableFixedProperty.set("properties", nonScalableFixedProperties);
        nonScalableFixedProperty.put("additionalProperties", false);
        return nonScalableFixedProperty;
    }

    private static ObjectNode createScalableFixedJsonObject()
    {
        ObjectNode scalableFixedProperty = mapper.createObjectNode();
        scalableFixedProperty.put("type", "object");

        ArrayNode required = mapper.createArrayNode();
        Arrays.asList("type", "value", "priority").forEach(required::add);
        scalableFixedProperty.set("required", required);

        ObjectNode scalableFixedProperties = mapper.createObjectNode();
        scalableFixedProperties.set("type", createStringTypeFieldWithValuePattern("^scalableFixed$"));
        scalableFixedProperties.set("value", createNumberTypeField());
        scalableFixedProperties.set("priority", createNumberTypeField());

        scalableFixedProperty.set("properties", scalableFixedProperties);
        scalableFixedProperty.put("additionalProperties", false);
        return scalableFixedProperty;
    }

    private static ObjectNode createNonScalableRangedJsonObject()
    {
        ObjectNode nonScalableRangedProperty = mapper.createObjectNode();
        nonScalableRangedProperty.put("type", "object");

        ArrayNode required = mapper.createArrayNode();
        Arrays.asList("type", "lower", "upper").forEach(required::add);
        nonScalableRangedProperty.set("required", required);

        ObjectNode nonScalableRangedProperties = mapper.createObjectNode();
        nonScalableRangedProperties.set("type", createStringTypeFieldWithValuePattern("^nonScalableRanged$"));
        nonScalableRangedProperties.set("lower", createNumberTypeField());
        nonScalableRangedProperties.set("upper", createNumberTypeField());

        nonScalableRangedProperty.set("properties", nonScalableRangedProperties);
        nonScalableRangedProperty.put("additionalProperties", false);
        return nonScalableRangedProperty;
    }

    private static ObjectNode createScalableRangedJsonObject()
    {
        ObjectNode scalableRangedProperty = mapper.createObjectNode();
        scalableRangedProperty.put("type", "object");

        ArrayNode required = mapper.createArrayNode();
        Arrays.asList("type", "lower", "upper", "priority").forEach(required::add);
        scalableRangedProperty.set("required", required);

        ObjectNode scalableRangedProperties = mapper.createObjectNode();
        scalableRangedProperties.set("type", createStringTypeFieldWithValuePattern("^scalableRanged$"));
        scalableRangedProperties.set("lower", createNumberTypeField());
        scalableRangedProperties.set("upper", createNumberTypeField());
        scalableRangedProperties.set("priority", createNumberTypeField());

        scalableRangedProperty.set("properties", scalableRangedProperties);
        scalableRangedProperty.put("additionalProperties", false);
        return scalableRangedProperty;
    }
}
