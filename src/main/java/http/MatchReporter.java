package http;

import clients.PoolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MatchReporter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchReporter.class);

    public MatchReporter()
    {
    }

    public static void reportMatch(Set<PoolClient> match)
    {
        List<Integer> matchedClientsIDs = new ArrayList<>();
        match.forEach(poolClient -> matchedClientsIDs.add(poolClient.getClientID()));

        LOGGER.info("Found match client IDs: {}", matchedClientsIDs);
    }
}
