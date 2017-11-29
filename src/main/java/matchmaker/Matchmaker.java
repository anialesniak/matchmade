package matchmaker;

import algorithm.MatchSearchTree;

public class Matchmaker
{
    public void run()
    {
        MatchSearchTree.getInstance().fillSearchTree();
        while (true) {
            MatchSearchTree.getInstance().matchIteration();
        }
    }
}
