package algorithm;

import clients.Client;
import clients.ClientSearchingData;
import com.google.common.collect.Sets;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import parameters.FixedParameter;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;
import parameters.ParameterRanges;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Stream;

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
        clientPool.getClients().forEach(client -> addClientToTree(client));
    }

    public void clearSearchTree() {
        clientsMatches.clear();
        searchTree = new KDTree(3); //TODO
    }

    public void fillClientsMatches() {
        clientPool.getClients().forEach(client -> clientsMatches.put(client.getClientID(), findMatchingSetFor(client)));
    }

    private void addClientToTree(Client client) {
        final double[] parametersArrayDouble = client.getSelfData().getParameters().values()
                .stream()
                .map(FixedParameter::getValue)
                .mapToDouble(Double::doubleValue)
                .toArray();
        searchTree.insert(parametersArrayDouble, client);
    }

    public Set<Client> findMatchingSetFor(Client client) {
        final double[] parametersArrayLowerDouble = client.getSearchingData().getParameters().values()
                .stream()
                .map(Parameter::getRanges).map(ParameterRanges::getLower)
                .mapToDouble(Double::doubleValue).toArray();
        final double[] parametersArrayUpperDouble = client.getSearchingData().getParameters().values()
                .stream()
                .map(Parameter::getRanges).map(ParameterRanges::getUpper)
                .mapToDouble(Double::doubleValue).toArray();
        final Set<Client> clientSet = new LinkedHashSet<>();
        Arrays.stream(searchTree.range(parametersArrayLowerDouble, parametersArrayUpperDouble))
                .map(Client.class::cast)
                .filter(match -> !match.equals(client))
                .forEach(clientSet::add);
        return clientSet;
    }

    public Set<Client> tryCreatingAMatchFrom(Client client, Set<Client> matches) {
        final Set<Client> processedMatches = new LinkedHashSet<>();
        matches.stream()
                .filter(currentClient -> clientsMatches.containsKey(currentClient.getClientID())
                && clientsMatches.get(currentClient.getClientID()).contains(client))
                .forEach(processedMatches::add);

        if (processedMatches.size() < teamSize - 1) return new HashSet<>();

        Set<Set<Client>> matchesCombinations = Sets.combinations(processedMatches, teamSize - 1);
        final Optional<Set<Client>> optionalCorrectMatch = matchesCombinations.stream().filter(this::isCorrectMatch).findFirst();
        if(optionalCorrectMatch.isPresent()) {
            LinkedHashSet<Client> correctMatch = new LinkedHashSet<>(optionalCorrectMatch.get());
            correctMatch.add(client);
            return correctMatch;
        }
        return new HashSet<>();
    }

    private boolean isCorrectMatch(Set<Client> match) {
        for (Client firstClient : match) {
            final Optional<Set<Client>> matchedClients = match.stream()
                    .filter(checkedClient -> !firstClient.equals(checkedClient))
                    .map(checkedClient -> clientsMatches.get(checkedClient.getClientID()))
                    .filter(checkedClientsSet -> !checkedClientsSet.contains(firstClient))
                    .findFirst();
            if (matchedClients.isPresent()) {
                matchedClients.get().remove(firstClient);
                return false;
            }
        }
        return true;
    }

    public void matchIteration() {
        clientsMatches.clear();
        fillClientsMatches();
        clientPool.getClients().stream()
                .map(client -> tryCreatingAMatchFrom(client, clientsMatches.get(client.getClientID())))
                .filter(match -> !match.isEmpty())
                .forEach(this::eraseMatchedClients);
    }

    private void eraseMatchedClients(Set<Client> match) {
        match.forEach(matchedClient -> clientsMatches.remove(matchedClient.getClientID()));
        clientPool.getClients().removeAll(match);
    }
}
