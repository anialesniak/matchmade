package algorithm;

import clients.Client;
import clients.ClientSearchingData;
import com.google.common.collect.Sets;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public final class MatchSearchTree {

    private final ClientPool clientPool;
    private final int teamSize = 3; //TODO
    private KDTree searchTree;
    private Map<Integer, Set<Client>> clientsMatches;

    @Inject
    public MatchSearchTree(final ClientPool clientPool)
    {
        this.clientPool = clientPool;
    }

    public void initializeSearchTree(){
        searchTree = new KDTree(3); //TODO
        clientsMatches = new HashMap<>();
    }

    public void fillSearchTree(){
        for (Client client : clientPool.getClients()) {
            addClientToTree(client);
        }
    }

    public void clearSearchTree() {
        clientsMatches.clear();
        searchTree = new KDTree(3); //TODO
    }

    public void fillClientsMatches() {
        for (Client client : clientPool.getClients()) {
            clientsMatches.put(client.getClientID(), findMatchingSetFor(client));
        }
    }

    private void addClientToTree(Client client) {
        final Map<String, NonScalableFixedParameter> parameters = client.getSelfData().getParameters();
        final double[] parametersArray = new double[parameters.size()];
        int index = 0;
        for (Map.Entry<String, NonScalableFixedParameter> parameter : parameters.entrySet()) {
                parametersArray[index++] = parameter.getValue().getValue();
        }
        searchTree.insert(parametersArray, client);
    }

    public Set<Client> findMatchingSetFor(Client client) {
        final ClientSearchingData searchingData = client.getSearchingData();
        final int parametersCount = searchingData.getParameters().size();
        final double[] parametersArrayLower = new double[parametersCount];
        final double[] parametersArrayUpper = new double[parametersCount];
        int index = 0;
        for (Parameter parameter : searchingData.getParameters().values()){
            parametersArrayLower[index] = parameter.getRanges().getLower();
            parametersArrayUpper[index] = parameter.getRanges().getUpper();
            index++;
        }
        final Object[] matches = searchTree.range(parametersArrayLower, parametersArrayUpper);
        final Set<Client> clientSet = new LinkedHashSet<>();
        for (Object object : matches) {
            if (!object.equals(client)) clientSet.add((Client) object);
        }
        return clientSet;
    }

    public Set<Client> tryCreatingAMatchFrom(Client client, Set<Client> matches) {
        final Set<Client> processedMatches = new LinkedHashSet<>();
        for (Client currentClient : matches) {
            if (clientsMatches.containsKey(currentClient.getClientID())
                    && clientsMatches.get(currentClient.getClientID()).contains(client)) {
                processedMatches.add(currentClient);
            }
        }
        if (processedMatches.size() < teamSize - 1) return new HashSet<>();
        Set<Set<Client>> matchesCombinations = Sets.combinations(processedMatches, teamSize - 1);
        for (Set<Client> currentMatch : matchesCombinations) {
            if (isCorrectMatch(currentMatch)) {
                LinkedHashSet<Client> correctMatch = new LinkedHashSet<>(currentMatch);
                correctMatch.add(client);
                return correctMatch;
            }
        }
        return new HashSet<>();
    }

    private boolean isCorrectMatch(Set<Client> match) {
        boolean correctMatch = true;
        matchCombinationLoop:
        for (Client firstClient : match) {
            for (Client checkedClient : match) {
                if (!firstClient.equals(checkedClient)) {
                    Set<Client> checkedClientsSet = clientsMatches.get(checkedClient.getClientID());
                    if (!checkedClientsSet.contains(firstClient)) {
                        checkedClientsSet.remove(firstClient);
                        correctMatch = false;
                        break matchCombinationLoop;
                    }
                }
            }
        }
        return correctMatch;
    }

    public void matchIteration() {
        clientsMatches.clear();
        fillClientsMatches();
        for (Client client : clientPool.getClients()) {
            final Set<Client> match = tryCreatingAMatchFrom(client, clientsMatches.get(client.getClientID()));
            if (!match.isEmpty()) {
                for (Client matchedClient: match) {
                    clientsMatches.remove(matchedClient.getClientID());
                }
                clientPool.getClients().removeAll(match);
            }
        }
    }
}
