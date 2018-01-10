#!/usr/local/bin/groovy
@Grapes(
        @Grab(group='com.squareup.okhttp3', module='okhttp', version='3.9.1')
)

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import groovy.transform.Field
import okhttp3.*

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.path.*

@Field String host
@Field int port
@Field String filePath

parseArgs(args)
String jsonBody = readFile(filePath)
sendRequest(host, port, jsonBody)

def parseArgs(String[] args)
{
    JCommander jCommander = new JCommander()
    def parameters = new EnrollParameters()
    jCommander.addObject(parameters)
    jCommander.parse(args)

    host = parameters.host
    port = parameters.port
    filePath = parameters.filePath
}

def readFile(String filePath)
{
    byte[] encoded = Files.readAllBytes(Paths.get(filePath))
    return new String(encoded, Charset.defaultCharset())
}

def sendRequest(String host, int port, String body)
{
    OkHttpClient client = new OkHttpClient()
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body)
    Request request = new Request.Builder()
                        .url("http://$host:$port")
                        .post(requestBody)
                        .build()
    Response response = client.newCall(request).execute()
    println response.body().string()
}

//def sendApachePost(String url, String body) {
//    CloseableHttpClient httpClient = HttpClientBuilder.create().build()
//    HttpPost request = new HttpPost(url)
//    StringEntity params = new StringEntity(body)
//    request.addHeader("content-type", "application/json")
//    request.setEntity(params)
//    httpClient.execute(request)
//    HttpResponse result = httpClient.execute(request)
//    println result
//}

@Parameters(separators = '=')
class EnrollParameters
{
    @Parameter(names = ['-h', '--host'])
    private final String host = 'localhost'

    @Parameter(names = ['-p', '--port'])
    private final int port = 8080

    @Parameter(names = ['-f', '--file'], required = true)
    private final String filePath
}

