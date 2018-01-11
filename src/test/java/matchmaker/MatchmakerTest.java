package matchmaker;

import algorithm.MatchSearchTree;
import algorithm.MatchSearchTreeTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(MockitoJUnitRunner.class)
public class MatchmakerTest {

    @Mock
    private MatchSearchTree matchSearchTree;

    @Test
    public void shouldStartWithoutException()
    {
        //given
        final Matchmaker matchmaker = new Matchmaker(matchSearchTree);
        //when
        final Throwable throwable = catchThrowable(matchmaker::run);
        //then
        assertThat(throwable).doesNotThrowAnyException();
    }
}
