package matchmaker;

import algorithm.MatchSearchTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Wrapper class for {@code Matchmaker} matchmaking functionality. Intended to be called by {@link Matchmaker#run()}.
 */
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

    public void run(){
        LOGGER.info("Matchmaker starting...");
        int i = 0;
        while (true) {
            i++;
            LOGGER.info("Iteration: " + i);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            searchTree.fillSearchTree();
            searchTree.matchIteration();
        }
    }
}
