package algorithm;

import clients.Client;
import clients.ClientSearchingData;
import com.google.common.collect.Sets;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;

import java.util.*;


public final class MatchSearchTree {

    private final int teamSize = 3; //TODO

    private static final MatchSearchTree INSTANCE = new MatchSearchTree();

    private KDTree searchTree;

    private Map<Integer, Set<Client>> clientsMatches;

    private MatchSearchTree(){
        searchTree = new KDTree(3); //TODO
        clientsMatches = new HashMap<>();
    }

    public static MatchSearchTree getInstance(){return INSTANCE;}

    public void fillSearchTree(){
        for (Client client : ClientPool.getSet()) {
            MatchSearchTree.getInstance().addClientToTree(client);
        }
    }

    public void fillClientsMatches(){
        for (Client client : ClientPool.getSet()) {
            clientsMatches.put(client.getClientID(), MatchSearchTree.getInstance().findMatchingSetFor(client));
        }
    }

    private void addClientToTree(Client client){
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
        final int parametersCount = searchingData.getParameters().size();
        double[] parametersArrayLower = new double[parametersCount];
        double[] parametersArrayUpper = new double[parametersCount];
        int index = 0;
        for (Parameter parameter : searchingData.getParameters().values()){
            parametersArrayLower[index] = parameter.getRanges().getLower();
            parametersArrayUpper[index] = parameter.getRanges().getUpper();
            index++;
        }
        final Object[] matches = searchTree.range(parametersArrayLower, parametersArrayUpper);
        final Set<Client> clientSet = new LinkedHashSet<>();
        for (Object object : matches) {
            clientSet.add((Client) object);
        }
        return clientSet;
    }

    public Set<Client> tryCreatingAMatchFrom(Client client, Set<Client> matches) {
        for (Client currentClient : matches) {
            if (!clientsMatches.containsKey(currentClient.getClientID())
                    || !clientsMatches.get(currentClient.getClientID()).contains(client)) {
                matches.remove(currentClient);
            }
        }
        if (matches.size() < teamSize - 1) return new HashSet<>();
        final Set<Set<Client>> matchesCombinations = Sets.combinations(matches, teamSize - 1);
        for (Set<Client> currentMatch : matchesCombinations) {
            boolean correctMatch = true;
            matchCombinationLoop:
            for (Client currentClient : currentMatch) {
                for (Client checkedClient : currentMatch) {
                    if (!currentClient.equals(checkedClient)) {
                        final Set<Client> checkedClientsSet = clientsMatches.get(checkedClient.getClientID());
                        if (!checkedClientsSet.contains(currentClient)) {
                            checkedClientsSet.remove(currentClient);
                            matchesCombinations.remove(currentMatch);
                            correctMatch = false;
                            break matchCombinationLoop;
                        }
                    }
                }
            }
            if(correctMatch){
                currentMatch.add(client);
                return currentMatch;
            }
        }
        return new HashSet<>();
    }

    public void matchIteration(){
        clientsMatches.clear();
        fillClientsMatches();
        for (Client client : ClientPool.getSet()){
            final Set<Client> match = tryCreatingAMatchFrom(client, clientsMatches.get(client.getClientID()));
            if (!match.isEmpty()){
                for (Client matchedClient: match) {
                    clientsMatches.remove(matchedClient.getClientID());
                }
                ClientPool.removeAll(match);
                System.out.println("We found a match!");
            }
        }
    }
}
