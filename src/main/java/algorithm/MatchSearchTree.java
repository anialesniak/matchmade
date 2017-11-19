package algorithm;

import clients.Client;
import net.sf.javaml.core.kdtree.KDTree;
import parameters.NonScalableFixedParameter;

import java.util.*;

/**
 * Created by kenjik on 19.11.17.
 */
public final class MatchSearchTree {

    private final KDTree searchTree;

    private MatchSearchTree(){
        searchTree = new KDTree(5); //TODO
    }

    private static final MatchSearchTree INSTANCE = new MatchSearchTree();

    public static MatchSearchTree getInstance(){return INSTANCE;}

    public void addClientToTree(Client client){
        final Map<String, NonScalableFixedParameter> parameters = client.getSelfData().getParameters();
        double[] parametersArray = new double[parameters.size()];
        int index = 0;
        for (Map.Entry<String, NonScalableFixedParameter> parameter : parameters.entrySet()){
                parametersArray[index++] = parameter.getValue().getValue();
        }
        searchTree.insert(parametersArray, client);
    }

    public Set<Client> findMatch(Client client){
        return null;
    }
}
