package rules;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientEnrollmentTestRule extends ExternalResource
{
    private final String host;
    private final int port;

    public ClientEnrollmentTestRule(final String host, final int port)
    {
        this.host = host;
        this.port = port;
    }

    public Response enrollClient(final String filePath)
    {
        final String body = readFile(filePath);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
        Request request = new Request.Builder()
                .url(String.format("http://%s:%d", host, port))
                .post(requestBody)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(final String filePath)
    {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, Charset.defaultCharset());
    }


}
