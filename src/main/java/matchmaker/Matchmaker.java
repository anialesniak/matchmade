package matchmaker;

import algorithm.MatchSearchTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Matchmaker
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Matchmaker.class);

    private final MatchSearchTree searchTree;

    @Inject
    Matchmaker(final MatchSearchTree searchTree)
    {
        this.searchTree = searchTree;
    }

    public void run()
    {
        LOGGER.info("Matchmaker starting...");
        searchTree.fillSearchTree();
        while (true) {
            searchTree.matchIteration();
        }
    }
}
