package de.goeuro.devtest.search.http;

import de.goeuro.devtest.exception.ApplicationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Queries an URL via GET and returns the reponse as String.
 *
 * "Accept: application/json" is used in the request.
 *
 * @author Rolf Schuster
 */
public class HttpLocationSearch {
    // don't bother with spring/dependency injection
    // because we want to keep the project simple and the JAR small
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * Queries the URL of the form:
     *      serviceUrl + "/" + searchParameter
     * and returns the response as String
     *
     * @param serviceUrl the URL to be called
     * @param searchParameter the search parameter to be appended to the URL as path
     *
     * @return tghe response as String
     *
     * @throws ApplicationException if there was an error accessing the query URL
     */
    public String searchLocation(String serviceUrl, String searchParameter) throws ApplicationException {
        if (!serviceUrl.endsWith("/")) {
            // some might not consider it a good practice to modify method parameters, but it can be useful
            serviceUrl += "/";
        }

        String url = null;

        CloseableHttpResponse response = null;
        try {
            url = serviceUrl + URLEncoder.encode(searchParameter, "UTF-8");

            HttpGet httpGet = new HttpGet(url);
            // request response in json format
            httpGet.setHeader("Accept", "application/json");
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                String msg = String.format("Server returned error code %s: %s",
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
                throw new IOException(msg);
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);

            EntityUtils.consume(entity);

            return result;
        } catch (IOException e) {
            throw new ApplicationException(ApplicationException.ErrorCode.INTERNAL_ERROR,
                    "Error while accessing " + url + " with cause: " + e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new ApplicationException(ApplicationException.ErrorCode.INTERNAL_ERROR,
                            "Error while closing http response object: " + e.getMessage(), e);
                }
            }
        }
    }

    // visible for testing purposes
    protected void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
