package guice;

import algorithm.MatchSearchTree;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import matchmaker.ClientPool;

/**
 * This is {@code Guice} module which injects dependencies throughout whole project. Methods from this class are not
 * supposed to be called 'by hand', only by internal {@code Guice} calls.
 */
public class MatchmadeModule extends AbstractModule
{
    @Override
    protected void configure()
    {
    }

    @Provides
    @Inject
    public MatchSearchTree provideSearchTree(final ClientPool clientPool)
    {
        final MatchSearchTree matchSearchTree = new MatchSearchTree(clientPool);
        matchSearchTree.initializeSearchTree();
        return matchSearchTree;
    }
}
