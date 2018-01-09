package validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import configuration.ConfigurationParameters;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class JSONValidatorTest
{
    @Test
    @Ignore("Left for easier inspection of built schema structure")
    public void prettyPrintRequestSchema() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        parameterNames.add("height");
        parameterNames.add("weight");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);

        String indentedJSON = new ObjectMapper().writerWithDefaultPrettyPrinter()
                                                .writeValueAsString(validator.getSchemaJsonNode());
        System.out.println(indentedJSON);
    }

    @Test
    public void validationOfValidJsonSucceeds() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        parameterNames.add("height");
        parameterNames.add("weight");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_valid.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void validationOfJsonWithTooManyParametersFails() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        parameterNames.add("height");
        parameterNames.add("weight");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_additionalParameters.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void validationOfJsonWithTooManyFieldsInParameterFails() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_additionalFieldsInParameter.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void validationOfJsonWithMissingFieldInParameterFails() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_missingFieldInParameter.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void validationOfJsonWithInvalidParameterTypeFails() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_invalidParameterType.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void validationOfJsonWithMissingClientSelfDataFails() throws Exception
    {
        // given
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        List<String> parameterNames = new ArrayList<>();
        parameterNames.add("age");
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        JSONValidator validator = new JSONValidator(configurationParameters);
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_missingClientSelfData.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        boolean validationResult = validator.isValid(validatedJson);

        // then
        assertThat(validationResult).isFalse();
    }
}