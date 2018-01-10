package http;

import com.google.common.io.Resources;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import matchmaker.ClientPool;
import org.eclipse.jetty.server.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import validation.InvalidRequestBodyException;
import validation.RequestBodyValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientRequestHandlerTest
{

    private static int CONTENT_LENGTH = 1560;

    @Mock
    private static HttpServletRequest request;
    @Mock
    private static HttpServletResponse response;
    @Mock
    private static Request baseRequest;
    @Mock
    private static ClientPool clientPool;
    @Mock
    private static Configuration configuration;
    @Mock
    private static ConfigurationParameters configurationParameters;
    @Mock
    private static RequestBodyValidator validator;

    @Test
    public void shouldHandleResponse() throws Exception
    {
        // given
        final File file = new File("src/test/resources/parameter-map.json");
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        given(request.getReader()).willReturn(reader);
        given(request.getContentLength()).willReturn(CONTENT_LENGTH);
        given(clientPool.getClients()).willReturn(new HashSet<>());
        given(configuration.getConfigurationParameters()).willReturn(configurationParameters);
        given(configurationParameters.getParameterNames()).willReturn(Arrays.asList("age", "weight", "height"));

        // when
        new ClientRequestHandler(clientPool, configuration).handle("", baseRequest, request, response);

        // then
        verify(request).getReader();
        verify(request, times(2)).getContentLength();
        verify(clientPool).getClients();
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void shouldThrowEmptyBodyException() throws Exception
    {
        // given
        given(request.getContentLength()).willReturn(0);
        given(configuration.getConfigurationParameters()).willReturn(configurationParameters);
        given(configurationParameters.getParameterNames()).willReturn(new ArrayList<>());

        // when
        final Throwable throwableWithEmptyBodyException =
                catchThrowable(() -> new ClientRequestHandler(clientPool, configuration).handle(
                    "",
                    baseRequest,
                    request,
                    response));
        // then
        assertThat(throwableWithEmptyBodyException).isInstanceOf(EmptyBodyException.class)
                                                   .hasMessage("No body was found in request.");
    }

    @Test
    public void shouldHandleResponseForInvalidRequestBody() throws Exception
    {
        // given
        String requestBody = Resources.toString(
                Resources.getResource("request_invalid_parameter_type.json"), StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(new StringReader(requestBody));
        given(request.getContentLength()).willReturn(CONTENT_LENGTH);
        given(request.getReader()).willReturn(reader);
        doThrow(new InvalidRequestBodyException("invalid")).when(validator).validate(any(String.class));

        // when
        new ClientRequestHandler(clientPool, configuration, validator).handle("", baseRequest, request, response);

        // then
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
