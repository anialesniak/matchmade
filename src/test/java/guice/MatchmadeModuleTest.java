package guice;

import algorithm.MatchSearchTree;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class MatchmadeModuleTest
{
    private final MatchmadeModule matchmadeModule = new MatchmadeModule();

    @Test
    public void shouldSuccessfullyInitializeModule()
    {
        //when
        final Throwable shouldBeEmptyThrowable = catchThrowable(() -> Guice.createInjector(matchmadeModule));
        //then
        assertThat(shouldBeEmptyThrowable).doesNotThrowAnyException();
    }

    @Test
    public void shouldProvideInitializedSearchTree()
    {
        //given
        final Injector injector = Guice.createInjector(matchmadeModule);
        //when
        final MatchSearchTree searchTree = injector.getInstance(MatchSearchTree.class);
        //then
        assertThat(searchTree)
                .isNotNull()
                .extracting(MatchSearchTree::isInitialized).containsOnly(true);
    }
}
