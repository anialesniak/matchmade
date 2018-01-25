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
    private static final int MINIMAL_ITERATION_TIME_IN_MILLIS = 2000;

    private final MatchSearchTree searchTree;

    @Inject
    Matchmaker(final MatchSearchTree searchTree)
    {
        this.searchTree = searchTree;
    }

    public void run(){
        LOGGER.debug("Matchmaker starting...");
        new Thread(() -> {
            int iterationCount = 0;
            while (true) {
                iterationCount++;
                LOGGER.debug("Iteration: {} Number of waiting clients: {}",
                        iterationCount,
                        searchTree.getNumberOfClientsToMatch());
                try {
                    Thread.sleep(MINIMAL_ITERATION_TIME_IN_MILLIS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                searchTree.fillSearchTree();
                searchTree.matchIteration();
            }
        }).start();
    }
}
