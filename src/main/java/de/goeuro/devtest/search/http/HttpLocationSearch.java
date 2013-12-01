package de.goeuro.devtest.search.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Rolf Schuster
 */
public class HttpLocationSearch {
    // don't bother with spring/dependency injection
    // because we want to keep the project simple and the über JAR small
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public String searchLocation(String serviceUrl) throws IOException {
        HttpGet httpGet = new HttpGet(serviceUrl);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String msg = String.format("Server returned error code %s: %s",
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new IOException(msg);
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            EntityUtils.consume(entity);

            return result;
        }
    }
}
