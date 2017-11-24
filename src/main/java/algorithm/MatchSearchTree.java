package algorithm;

import clients.Client;
import clients.ClientSearchingData;
import net.sf.javaml.core.kdtree.KDTree;
import parameters.NonScalable;
import parameters.NonScalableFixedParameter;
import parameters.Scalable;

import java.util.*;


public final class MatchSearchTree {

    private final int teamSize = 3; //TODO

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

    public Set<Client> findMatchingSetFor(Client client){
        final ClientSearchingData searchingData = client.getSearchingData();
        final int parametersCount = searchingData.getScalableParameters().size() + searchingData.getNonScalableParameters().size();
        double[] parametersArrayLower = new double[parametersCount];
        double[] parametersArrayUpper = new double[parametersCount];
        int index = 0;
        for (Scalable scalableParameter : searchingData.getScalableParameters().values()){
            parametersArrayLower[index] = scalableParameter.getRanges().getLower();
            parametersArrayUpper[index] = scalableParameter.getRanges().getUpper();
            index++;
        }
        for(NonScalable nonScalableParameter : searchingData.getNonScalableParameters().values()){
            parametersArrayLower[index] = nonScalableParameter.getRanges().getLower();
            parametersArrayUpper[index] = nonScalableParameter.getRanges().getUpper();
            index++;
        }
        final Client[] matches = (Client[])searchTree.range(parametersArrayLower, parametersArrayUpper);
        return new LinkedHashSet<>(Arrays.asList(matches));
    }

    public Set<Client> tryCreatingAMatchFrom(Client client, Set<Client> matches){
        if (matches.size()<teamSize-1) return new HashSet<>();
        
    }
}
