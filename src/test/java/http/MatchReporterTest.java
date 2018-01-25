package http;

import clients.PoolClient;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class MatchReporterTest
{
    private Set<PoolClient> dummyClientSet;

    @Before
    public void setUp()
    {
        dummyClientSet = ImmutableSet.of(
                mock(PoolClient.class)
        );
    }

    @Test
    public void shouldReportWithNoException()
    {
        //when
        final Throwable throwableWithNoException = catchThrowable(() -> Reporter.logMatch(dummyClientSet));
        //then
        assertThat(throwableWithNoException).doesNotThrowAnyException();
        dummyClientSet.forEach(
                poolClient -> {
                    verify(poolClient).getClientID();
                    verifyNoMoreInteractions(poolClient);
                }
        );
    }
}
