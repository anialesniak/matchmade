package matchmaker;

import algorithm.MatchSearchTree;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Matchmaker
{
    private final MatchSearchTree searchTree;

    @Inject
    Matchmaker(final MatchSearchTree searchTree)
    {
        this.searchTree = searchTree;
    }

    public void run()
    {
        searchTree.fillSearchTree();
        while (true) {
            searchTree.matchIteration();
        }
    }
}
