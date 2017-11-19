package http;

import clients.Client;
import clients.ClientSearchingData;
import clients.ClientSelfData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import matchmaker.ClientPool;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import parameters.NonScalableFixedParameter;
import parameters.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class ClientRequestHandler extends AbstractHandler
{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<Map<String, Map<String, NonScalableFixedParameter>>> nonScalableTypeReference =
            new TypeReference<Map<String, Map<String, NonScalableFixedParameter>>>() {};
    private final TypeReference<Map<String, Map<String, Parameter>>> nonScalableTypeReference =
            new TypeReference<Map<String, Map<String, Parameter>>>() {};

    @Override
    public void handle(final String target,
                       final Request baseRequest,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws IOException, ServletException
    {
        final String body = extractBody(request);
        final Client client = convertToClient(body);
        ClientPool.getInstance().getClientSet().add(client);
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
        int read = 0, readTo = 0;
        while (read > -1) {
            read = reader.read(buf, readTo, 1000);
            readTo += read;
        }
        return new String(buf);
    }

    private Client convertToClient(final String jsonBody) throws IOException
    {
        final Map<String, Map<String, Parameter>> parameterMap =
                objectMapper.readValue(jsonBody, new TypeReference<Map<String, Map<String, Parameter>>>() {});
        final ClientSelfData clientSelf = new ClientSelfData(parameterMap.get("clientSelf"));
        final ClientSearchingData clientSearching = new ClientSearchingData(parameterMap.get("clientSearching"));
        return new Client(clientSelf, clientSearching);
    }
}
