package de.goeuro.devtest.search.http;

import de.goeuro.devtest.exception.ApplicationException;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import static org.testng.Assert.*;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Tests that HttpLocationSearch behaves in the expected way (HTTP status codes, IOExceptions, URL escaping...).
 *
 * @author Rolf Schuster
 */
@Test
public class HttpLocationSearchTest {

    private static final String URL = "http://something/service";
    private static final String PARAM = "Cluj";
    private static final String RESULT = "query result";

    @Mock
    private CloseableHttpClient httpClient;

    @InjectMocks
    private HttpLocationSearch target;

    @BeforeMethod
    public void setup() {
        target = new HttpLocationSearch();
        // init the httpClient property
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Happy flow.
     *
     * @throws ApplicationException
     * @throws IOException
     */
    public void searchLocationOk() throws ApplicationException, IOException {
        // setup
        mockHttpClient(HttpStatus.SC_OK, RESULT);

        // execute
        String result = target.searchLocation(URL, PARAM);

        //verify
        assertEquals(result, RESULT);
        // check that application/json has been requested as content
        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture());
        Header header = captor.getValue().getFirstHeader("Accept");
        assertNotNull(header);
        assertTrue(header.getValue().contains("application/json"));
        // check that the URL is OK
        assertEquals(captor.getValue().getURI().toString(), URL + "/" + PARAM);
    }

    public void searchLocationNotDoubleSlashInUrl() throws ApplicationException, IOException {
        // setup
        mockHttpClient(HttpStatus.SC_OK, RESULT);

        // execute
        // also add a slash to the URL
        target.searchLocation(URL + "/", PARAM);

        //verify
        // check that the URL has no double slash, like http://something/service//Cluj
        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture());
        assertEquals(captor.getValue().getURI().toString(), URL + "/" + PARAM);
    }

    public void searchLocationEscapeParameter() throws ApplicationException, IOException {
        // setup
        mockHttpClient(HttpStatus.SC_OK, RESULT);

        // execute
        // also add a slash to the URL
        target.searchLocation(URL, "Frankfurt am Main");

        //verify
        // check that the URL is properly escaped
        ArgumentCaptor<HttpGet> captor = ArgumentCaptor.forClass(HttpGet.class);
        verify(httpClient).execute(captor.capture());
        assertEquals(captor.getValue().getURI().toString(), URL + "/" + "Frankfurt+am+Main");
    }

    @Test(expectedExceptions = ApplicationException.class)
    public void searchLocationErrorStatus() throws ApplicationException, IOException {
        mockHttpClient(HttpStatus.SC_BAD_REQUEST, RESULT);

        target.searchLocation(URL, PARAM);

    }

    @Test(expectedExceptions = ApplicationException.class)
    public void searchLocationIOException() throws ApplicationException, IOException {
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException());

        target.searchLocation(URL, PARAM);

    }

    // setup the httpClient to respond with a given status code and response String
    private void mockHttpClient(int statusCode, String responseString) throws IOException {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("", 0, 0), statusCode, "");
        when(response.getStatusLine()).thenReturn(statusLine);

        StringEntity entity = new StringEntity(responseString, ContentType.APPLICATION_JSON);
        when(response.getEntity()).thenReturn(entity);

        when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
    }
}
