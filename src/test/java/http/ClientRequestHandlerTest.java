package http;

import matchmaker.ClientPool;
import org.eclipse.jetty.server.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClientRequestHandlerTest {

    private static int CONTENT_LENGTH = 1560;

    @Mock
    private static HttpServletRequest request;
    @Mock
    private static HttpServletResponse response;
    @Mock
    private static Request baseRequest;
    @Mock
    private static ClientPool clientPool;

    @Test
    public void shouldHandleResponse() throws Exception {
        //given
        final File file = new File("src/test/resources/parameter-map.json");
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        given(request.getReader()).willReturn(reader);
        given(request.getContentLength()).willReturn(CONTENT_LENGTH);
        given(clientPool.getClients()).willReturn(new HashSet<>());
        //when
        new ClientRequestHandler(clientPool).handle("", baseRequest, request, response);
        //then
        verify(request).getReader();
        verify(request, times(2)).getContentLength();
        verify(clientPool).getClients();
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void shouldThrowEmptyBodyException() throws Exception {
        //given
        given(request.getContentLength()).willReturn(0);
        //when
        final Throwable throwableWithEmptyBodyException = catchThrowable(
                () -> new ClientRequestHandler(clientPool).handle("", baseRequest, request, response));
        //then
        assertThat(throwableWithEmptyBodyException)
                .isInstanceOf(EmptyBodyException.class)
                .hasMessage("No body was found in request.");
    }
}
