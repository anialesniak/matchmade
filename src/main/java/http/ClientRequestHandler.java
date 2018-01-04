package http;

import clients.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import matchmaker.ClientPool;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Entry point of {@code Matchmade} from client request point of view. This class handles http requests sent by
 * client while looking for a match. Depends on {@code Jetty} http server implementation.
 */
@Singleton
public class ClientRequestHandler extends AbstractHandler
{
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientRequestHandler.class);

    private final ClientPool clientPool;

    @Inject
    ClientRequestHandler(final ClientPool clientPool)
    {
        this.clientPool = clientPool;
    }

    /**
     * Is called when server receives an http request. Then http request is being validated. If valid, body in the
     * form of JSON is deserialized and converted to client instance and added to the {@link ClientPool}.
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(final String target,
                       final Request baseRequest,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws IOException, ServletException
    {
        LOGGER.info("Received temporaryClient request");
        final String body = extractBody(request);
        final TemporaryClient temporaryClient = convertToClient(body);
        LOGGER.info("Request converted to temporaryClient: {}", temporaryClient);
        clientPool.getClients().add(new PoolClient(temporaryClient));
        response.setStatus(HttpServletResponse.SC_OK);
        LOGGER.info("TemporaryClient added to pool, returning with status 200.");
    }

    private String extractBody(final HttpServletRequest request) throws IOException
    {
        if (request.getContentLength() > 0) {
            return readBody(request);
        } else {
            throw new EmptyBodyException("No body was found in request.");
        }
    }

    private String readBody(final HttpServletRequest request) throws IOException
    {
        final BufferedReader reader = request.getReader();
        int len = request.getContentLength();
        char[] buf = new char[len];
        reader.read(buf);
        return new String(buf);
    }

    private TemporaryClient convertToClient(final String jsonBody) throws IOException
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Map<String, Parameter>> parameterMap =
                objectMapper.readValue(jsonBody, new TypeReference<Map<String, Map<String, Parameter>>>() {});

        final ClientSelfData clientSelf = new ClientSelfData(objectMapper.convertValue(
                parameterMap.get(ClientDataType.CLIENT_SELF.getTypeName()),
                new TypeReference<Map<String, NonScalableFixedParameter>>() {}));
        final ClientSearchingData clientSearching =
                new ClientSearchingData(parameterMap.get(ClientDataType.CLIENT_SEARCHING.getTypeName()));

        return new TemporaryClient(clientSelf, clientSearching);
    }
}
