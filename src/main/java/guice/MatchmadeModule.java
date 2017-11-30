package guice;

import algorithm.MatchSearchTree;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import matchmaker.ClientPool;

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
