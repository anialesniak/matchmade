package validation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import configuration.ConfigurationParameters;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class JSONRequestBodyValidatorTest
{
    @Test
    public void validationOfValidJsonSucceeds() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age", "height", "weight"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_valid.json"), StandardCharsets.UTF_8);

        // when
        Throwable shouldNotThrowException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldNotThrowException).doesNotThrowAnyException();
    }

    @Test
    public void validationOfJsonWithTooManyParametersFails() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age", "height", "weight"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_additional_parameters.json"), StandardCharsets.UTF_8);

        // when
        Throwable shouldThrowInvalidRequestBodyException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldThrowInvalidRequestBodyException).isInstanceOf(InvalidRequestBodyException.class);
    }

    @Test
    public void validationOfJsonWithTooManyFieldsInParameterFails() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_additional_fields_in_parameter.json"), StandardCharsets.UTF_8);

        // when
        Throwable shouldThrowInvalidRequestBodyException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldThrowInvalidRequestBodyException).isInstanceOf(InvalidRequestBodyException.class);
    }

    @Test
    public void validationOfJsonWithMissingFieldInParameterFails() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_missing_field_in_parameter.json"), StandardCharsets.UTF_8);

        // when
        Throwable shouldThrowInvalidRequestBodyException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldThrowInvalidRequestBodyException).isInstanceOf(InvalidRequestBodyException.class);
    }

    @Test
    public void validationOfJsonWithInvalidParameterTypeFails() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_invalid_parameter_type.json"), StandardCharsets.UTF_8);
        JsonNode validatedJson = new ObjectMapper().readTree(validatedJsonBody);

        // when
        Throwable shouldThrowInvalidRequestBodyException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldThrowInvalidRequestBodyException).isInstanceOf(InvalidRequestBodyException.class);
    }

    @Test
    public void validationOfJsonWithMissingClientSelfDataFails() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age"));
        String validatedJsonBody = Resources.toString(
                Resources.getResource("request_missing_client_self_data.json"), StandardCharsets.UTF_8);

        // when
        Throwable shouldThrowInvalidRequestBodyException = catchThrowable(() -> validator.validate(validatedJsonBody));

        // then
        assertThat(shouldThrowInvalidRequestBodyException).isInstanceOf(InvalidRequestBodyException.class);
    }

    @Test
    public void shouldThrowJsonParseExceptionForInvalidRequestBodyFormat() throws Exception
    {
        // given
        RequestBodyValidator validator = createJsonRequestBodyValidatorFor(Arrays.asList("age"));
        String invalidRequestBody = "{invalid json}";

        // when
        Throwable throwableWithJsonParseException = catchThrowable(() -> validator.validate(invalidRequestBody));

        // then
        assertThat(throwableWithJsonParseException.getCause()).isInstanceOf(JsonParseException.class);
    }

    private RequestBodyValidator createJsonRequestBodyValidatorFor(List<String> parameterNames)
    {
        ConfigurationParameters configurationParameters = mock(ConfigurationParameters.class, withSettings().stubOnly());
        when(configurationParameters.getParameterNames()).thenReturn(parameterNames);
        return new JSONRequestBodyValidator(configurationParameters);
    }
}