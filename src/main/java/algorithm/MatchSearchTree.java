package algorithm;

import clients.PoolClient;
import com.google.common.collect.Sets;
import configuration.Configuration;
import http.MatchReporter;
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
 * Class responsible for matching {@link PoolClient}s. This is where the algorithm is being used. Mind that to work class
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
    private Map<Integer, Set<PoolClient>> clientsMatches;

    @Inject
    public MatchSearchTree(final ClientPool clientPool, final Configuration configuration)
    {
        this.clientPool = clientPool;
        this.teamSize = configuration.getConfigurationParameters().getTeamSize();
        this.parametersCount = configuration.getConfigurationParameters().getParameterCount();
    }

    MatchSearchTree(final ClientPool clientPool,
                    final Configuration configuration,
                    final Map<Integer, Set<PoolClient>> clientsMatches,
                    final KDTree searchTree)
    {
        this.clientPool = clientPool;
        this.teamSize = configuration.getConfigurationParameters().getTeamSize();
        this.parametersCount = configuration.getConfigurationParameters().getParameterCount();
        this.clientsMatches = clientsMatches;
        this.searchTree = searchTree;
    }

    public int getNumberOfClientsToMatch()
    {
        return clientPool.getNumberOfClientsInPool();
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
        clientPool.expandClientsParameters();
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


    private void addClientToTree(PoolClient client)
    {
        final double[] parametersArrayDouble = client.getSelfData().getParameters().values()
                .stream()
                .map(FixedParameter::getValue)
                .mapToDouble(Double::doubleValue)
                .toArray();
        searchTree.insert(parametersArrayDouble, client);
    }

    public Set<PoolClient> findMatchingSetFor(PoolClient client)
    {
        final double[] parametersArrayLowerDouble = client.getPrioritizedSearchingData().getParameters().values()
                .stream()
                .map(Parameter::getRanges).map(ParameterRanges::getLower)
                .mapToDouble(Double::doubleValue).toArray();
        final double[] parametersArrayUpperDouble = client.getPrioritizedSearchingData().getParameters().values()
                .stream()
                .map(Parameter::getRanges).map(ParameterRanges::getUpper)
                .mapToDouble(Double::doubleValue).toArray();
        final Set<PoolClient> clientSet = new LinkedHashSet<>();
        Arrays.stream(searchTree.range(parametersArrayLowerDouble, parametersArrayUpperDouble))
                .map(PoolClient.class::cast)
                .filter(match -> !match.equals(client))
                .forEach(clientSet::add);
        //LOGGER.info("Matching set for client: {} is {}", client, clientSet);
        return clientSet;
    }

    public Set<PoolClient> tryCreatingAMatchFrom(PoolClient client, Set<PoolClient> matches)
    {
        final Set<PoolClient> processedMatches = filterClientsThatDontMatchTo(client, matches);

        if (processedMatches.size() < teamSize - 1) return new HashSet<>();

        final Set<PoolClient> correctMatch = new LinkedHashSet<>();
        Sets.combinations(processedMatches, teamSize - 1)
                .stream()
                .filter(this::isCorrectMatch)
                .findFirst()
                .ifPresent((match) -> {
                    correctMatch.addAll(match);
                    correctMatch.add(client);
                    MatchReporter.reportMatch(correctMatch);
                });
        return correctMatch;
    }

    private Set<PoolClient> filterClientsThatDontMatchTo(PoolClient client, Set<PoolClient> matches)
    {
        final Set<PoolClient> processedMatches = new LinkedHashSet<>();

        if (matches != null)matches.stream()
                .filter(currentClient -> doesMatch(client, currentClient))
                .forEach(processedMatches::add);
        return processedMatches;
    }

    private boolean doesMatch(PoolClient client, PoolClient checkedClient)
    {
        return clientsMatches.containsKey(checkedClient.getClientID())
                && clientsMatches.get(checkedClient.getClientID()).contains(client);
    }

    private boolean isCorrectMatch(Set<PoolClient> match)
    {
        for (PoolClient firstClient : match) {
            final Optional<Set<PoolClient>> matchedClients = match.stream()
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

    private void eraseMatchedClients(Set<PoolClient> match)
    {
        match.forEach(matchedClient -> clientsMatches.remove(matchedClient.getClientID()));
        clientPool.getClients().removeAll(match);
        for (PoolClient client:match) {
            final double[] parametersArrayDouble = client.getSelfData().getParameters().values()
                    .stream()
                    .map(FixedParameter::getValue)
                    .mapToDouble(Double::doubleValue)
                    .toArray();
            searchTree.delete(parametersArrayDouble);
        }
    }
}
