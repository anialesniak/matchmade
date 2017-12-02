package http;

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

    @Test
    public void shouldStartWithNoException() throws Exception
    {
        //when
        final Throwable throwable = catchThrowable(() -> new MatchmadeServer(requestHandler).run());
        //then
        assertThat(throwable).doesNotThrowAnyException();
    }
}
