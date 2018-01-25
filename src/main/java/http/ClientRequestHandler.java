package http;

import clients.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.Configuration;
import configuration.ConfigurationParameters;
import matchmaker.ClientPool;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;
import validation.InvalidRequestBodyException;
import validation.JSONRequestBodyValidator;
import validation.RequestBodyValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Entry point of {@code Matchmade} from client request point of view. This class handles http requests sent by client
 * while looking for a match. Depends on {@code Jetty} http server implementation.
 */
@Singleton
public class ClientRequestHandler extends AbstractHandler
{
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientRequestHandler.class);

    private final ClientPool clientPool;
    private final Configuration configuration;
    private final RequestBodyValidator validator;

    @Inject
    public ClientRequestHandler(final ClientPool clientPool, final Configuration configuration)
    {
        this.clientPool = clientPool;
        this.configuration = configuration;
        this.validator = new JSONRequestBodyValidator(configuration.getConfigurationParameters());
    }

    ClientRequestHandler(final ClientPool clientPool,
                         final Configuration configuration,
                         final RequestBodyValidator validator)
    {
        this.clientPool = clientPool;
        this.configuration = configuration;
        this.validator = validator;
    }

    /**
     * Is called when server receives an http request. Then http request is being validated. If valid, body in the form
     * of JSON is deserialized and converted to client instance and added to the {@link ClientPool}.
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
                       final HttpServletResponse response) throws IOException, ServletException
    {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "accept, content-type, Access-Control-Allow-Origin");
        response.addHeader("Access-Control-Max-Age", "1728000");
        if (request.getMethod() != null && request.getMethod().equals("OPTIONS"))
        {
            //In case of an OPTIONS, we allow the access to the origin of the petition
            System.out.println("Options got");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }
        else {
            LOGGER.debug("Received temporaryClient request");
            final String body = extractBody(request);
            handleRequestBody(body, response);
            baseRequest.setHandled(true);
        }
    }

    private void handleRequestBody(String body, HttpServletResponse response) throws IOException
    {
        try {
            validator.validate(body);
            LOGGER.debug("Request validated successfully");
            handleValidRequestBody(body);
            response.setStatus(HttpServletResponse.SC_OK);
            LOGGER.debug("PoolClient added to pool, returning with status 200");
        }
        catch (InvalidRequestBodyException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOGGER.warn("Request failed validation, returning with status 400");
        }

    }

    private String extractBody(final HttpServletRequest request) throws IOException
    {
        if (request.getContentLength() > 0) {
            return readBody(request);
        }
        else {
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

    private void handleValidRequestBody(String body) throws IOException
    {
        ConfigurationParameters configurationParameters = configuration.getConfigurationParameters();
        final PoolClient poolClient = PoolClient.builder()
                                                .withTemporaryClient(convertToTemporaryClient(body))
                                                .withConfigurationParameters(configurationParameters)
                                                .build();
        LOGGER.debug("Request converted to poolClient with ID = {}", poolClient.getClientID());
        clientPool.getClients().add(poolClient);
    }

    private TemporaryClient convertToTemporaryClient(final String jsonBody) throws IOException
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
