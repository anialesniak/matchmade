package http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

@RunWith(MockitoJUnitRunner.class)
public class MatchmadeServerTest
{
    private static MatchmadeServer matchmadeServer;

    @Before
    public void setUp()
    {
        matchmadeServer = new MatchmadeServer(mock(ClientRequestHandler.class, withSettings().stubOnly()));
    }

    @After
    public void stopServer()
    {
        matchmadeServer.stop();
    }

    @Test
    public void shouldStartWithNoException()
    {
        //given
        //when
        final Throwable throwableWithNoException = catchThrowable(() -> matchmadeServer.run());
        //then
        assertThat(throwableWithNoException).doesNotThrowAnyException();
    }
}
