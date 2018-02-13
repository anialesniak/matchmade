package http;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class MatchmadeServerTest
{
    @Mock
    private ClientRequestHandler requestHandler;
    private MatchmadeServer matchmadeServer = new MatchmadeServer(requestHandler);

    @After
    public void stopServer()
    {
        matchmadeServer.stop();
    }

    @Test
    public void shouldStartWithNoException()
    {
        //when
        final Throwable throwableWithNoException = catchThrowable(() -> matchmadeServer.run());
        //then
        assertThat(throwableWithNoException).doesNotThrowAnyException();
    }
}
