package matchmaker;

import algorithm.MatchSearchTree;
import clients.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Matchmaker
{
    public void run()
    {
        MatchSearchTree.getInstance().fillSearchTree();
        while(true){
            MatchSearchTree.getInstance().matchIteration();
        }
    }
}
