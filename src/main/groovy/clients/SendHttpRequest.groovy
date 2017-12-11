package clients

import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.codehaus.groovy.runtime.InvokerHelper


/**
 * Created by annterina on 11.12.17.
 */
class SendHttpRequest extends Script {

    def sendApachePost(String url, String body) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build()
        HttpPost request = new HttpPost(url)
        StringEntity params = new StringEntity(body)
        request.addHeader("content-type", "application/json")
        request.setEntity(params)
        httpClient.execute(request)
        HttpResponse result = httpClient.execute(request)
        println result
    }

    def sendJavaPost(String url, String body) throws Exception {

        URL urlObject = new URL(url)
        HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection()

        connection.setRequestMethod("POST")
        connection.setRequestProperty("Content-Type","application/json")

        connection.setDoOutput(true)

        byte[] outputInBytes = body.getBytes("UTF-8")
        OutputStream os = connection.getOutputStream()
        os.write(outputInBytes)
        os.flush()
        os.close()

        int responseCode = connection.getResponseCode()
        System.out.println("\nSending 'POST' request to URL : " + url)
        System.out.println("Response Code : " + responseCode)

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))
        String inputLine
        StringBuffer response = new StringBuffer()

        while ((inputLine = input.readLine()) != null) response.append(inputLine)
        input.close()

        System.out.println(response.toString())

    }

    def run() {
        String jsonFileContent =  new File(args[2].toString()).getText('UTF-8')
        sendApachePost(args[0]+":"+args[1], jsonFileContent)
    }

    static void main(String[] args) {
        InvokerHelper.runScript(SendHttpRequest, args)
    }
}

