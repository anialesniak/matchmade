package http;

import clients.PoolClient;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Reporter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Reporter.class);
    private static final String ENROLLMENT_URL = "http://localhost:8080/send/matchmade/enrollment";
    private static final String MATCH_URL = "http://localhost:8080/send/matchmade/match";

    public static void logMatch(Set<PoolClient> match)
    {
        LOGGER.info("Found match client IDs: {}", convertMatchToClientIDsList(match));
    }

    private static List<Long> convertMatchToClientIDsList(Set<PoolClient> match)
    {
        List<Long> matchedClientsIDs = new ArrayList<>();
        match.forEach(poolClient -> matchedClientsIDs.add(poolClient.getClientID()));
        return matchedClientsIDs;
    }

    public static void reportEnrolledClient(PoolClient client)
    {
        RequestBody enrollment = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client-id", String.format("%d", client.getClientID()))
                .build();
        Request enrollRequest = new Request.Builder()
                .url(ENROLLMENT_URL)
                .post(enrollment)
                .build();

        try {
            new OkHttpClient().newCall(enrollRequest).execute();
        } catch (IOException e) {
            LOGGER.error("Could not send enrollment");
        }
    }

    public static void reportMatch(Set<PoolClient> match)
    {
        List<Long> clientIDs = convertMatchToClientIDsList(match);

        RequestBody matchReport = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("match", formatMatchParameter(clientIDs))
                .build();
        Request matchReportRequest = new Request.Builder()
                .url(MATCH_URL)
                .post(matchReport)
                .build();

        try {
            new OkHttpClient().newCall(matchReportRequest).execute();
        } catch (IOException e) {
            LOGGER.error("Could not send match report");
        }
    }

    private static String formatMatchParameter(List<Long> clientIDs)
    {
        StringBuilder encodedListBuilder = new StringBuilder();
        clientIDs.forEach(clientID -> encodedListBuilder.append(clientID).append(';'));

        String encodedList = encodedListBuilder.toString();
        encodedList = encodedList.substring(0, encodedList.length() - 1);
        return encodedList;
    }
}
