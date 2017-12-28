package algorithm;

import clients.Client;
import com.google.common.collect.Sets;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import matchmaker.ClientPool;
import net.sf.javaml.core.kdtree.KDTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parameters.FixedParameter;
import parameters.Parameter;
import parameters.ParameterRanges;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Class responsible for matching {@link Client}s. This is where the algorithm is being used. Mind that to work class
 * needs to be properly initialized by calling {@link MatchSearchTree#initializeSearchTree()}.
 */
@Singleton
public class MatchSearchTree
{

    private final static Logger LOGGER = LoggerFactory.getLogger(MatchSearchTree.class);

    private final ClientPool clientPool;
    private final int teamSize;
    private final int parametersCount;
    private KDTree searchTree;
    private Map<Integer, Set<Client>> clientsMatches;

    @Inject
    public MatchSearchTree(final ClientPool clientPool, final Configuration configuration)
    {
        this.clientPool = clientPool;
        this.teamSize = configuration.getConfigurationParameters().getTeamSize();
        this. parametersCount = configuration.getConfigurationParameters().getParameterCount();
    }

    public boolean isInitialized()
    {
        return searchTree != null && clientsMatches != null;
    }

    //TODO I actually think this is worse than calling it in a constructor. I am aware this was also my idea to
    // extract this from constructor, maybe use static factory method here for initialization which will be called in
    // guice? :^) - #cleancodingneverends #sorryforbeingmaniac
    public void initializeSearchTree()
    {
        LOGGER.info("Search tree initialized");
        searchTree = new KDTree(parametersCount);
        clientsMatches = new HashMap<>();
    }

    public void matchIteration()
    {
        clientsMatches.clear();
        fillClientsMatches();
        clientPool.getClients().stream()
                .map(client -> tryCreatingAMatchFrom(client, clientsMatches.get(client.getClientID())))
                .filter(match -> !match.isEmpty())
                .forEach(this::eraseMatchedClients);
    }

    public void fillSearchTree()
    {
        clientPool.getClients().forEach(this::addClientToTree);
    }

    public void clearSearchTree()
    {
        clientsMatches.clear();
        searchTree = new KDTree(parametersCount);
    }

    public void fillClientsMatches()
    {
        clientPool.getClients().forEach(client -> clientsMatches.put(client.getClientID(), findMatchingSetFor(client)));
    }


    private void addClientToTree(Client client)
    {
        final double[] parametersArrayDouble = client.getSelfData().getParameters().values()
                .stream()
                .map(FixedParameter::getValue)
                .mapToDouble(Double::doubleValue)
                .toArray();
        searchTree.insert(parametersArrayDouble, client);
    }

    public Set<Client> findMatchingSetFor(Client client)
    {
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
        LOGGER.info("Matching set for client: {} is {}", client, clientSet);
        return clientSet;
    }

    public Set<Client> tryCreatingAMatchFrom(Client client, Set<Client> matches)
    {
        final Set<Client> processedMatches = filterClientsThatDontMatchTo(client, matches);

        if (processedMatches.size() < teamSize - 1) return new HashSet<>();

        final Set<Client> correctMatch = new LinkedHashSet<>();
        Sets.combinations(processedMatches, teamSize - 1)
                .stream()
                .filter(this::isCorrectMatch)
                .findFirst()
                .ifPresent((match) -> {
                    correctMatch.addAll(match);
                    correctMatch.add(client);
                    LOGGER.info("Found match: {}", correctMatch);
                });
        return correctMatch;
    }

    private Set<Client> filterClientsThatDontMatchTo(Client client, Set<Client> matches)
    {
        final Set<Client> processedMatches = new LinkedHashSet<>();
        matches.stream()
                .filter(currentClient -> doesMatch(client, currentClient))
                .forEach(processedMatches::add);
        return processedMatches;
    }

    private boolean doesMatch(Client client, Client checkedClient)
    {
        return clientsMatches.containsKey(checkedClient.getClientID())
                && clientsMatches.get(checkedClient.getClientID()).contains(client);
    }

    private boolean isCorrectMatch(Set<Client> match)
    {
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

    private void eraseMatchedClients(Set<Client> match)
    {
        match.forEach(matchedClient -> clientsMatches.remove(matchedClient.getClientID()));
        clientPool.getClients().removeAll(match);
    }
}
