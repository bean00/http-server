package com.bean00.server;

import com.bean00.datastore.DataStore;
import com.bean00.request.Request;
import com.bean00.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestProcessorTest {
    private Request GETRequest = new Request("GET", "/file1", new ArrayList<>());
    private Request HEADRequest = new Request("HEAD", "/file1", new ArrayList<>());
    private DataStore dataStore;
    private RequestProcessor requestProcessor;

    @BeforeEach
    public void setup() throws IOException {
        dataStore = mock(DataStore.class);
        requestProcessor = new RequestProcessor(dataStore);
        when(dataStore.dataCanBeFound("/file1")).thenReturn(true);
        when(dataStore.getData("/file1")).thenReturn("file1 contents".getBytes());
        when(dataStore.getMediaType("/file1")).thenReturn("text/plain");
    }

    @Test
    public void processRequest_returns200_forTheRootPath() throws IOException {
        int expectedStatusCode = 200;
        Request rootRequest = new Request("GET", "/", new ArrayList<>());

        Response response = requestProcessor.processRequest(rootRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns404_ifTheURLIsInvalid() throws IOException {
        int expectedStatusCode = 404;
        Request invalidHEADRequest = new Request("HEAD", "/foobar", new ArrayList<>());
        when(dataStore.dataCanBeFound("/foobar")).thenReturn(false);

        Response response = requestProcessor.processRequest(invalidHEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns200_forAValidRequest_forHEAD() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(HEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsContentLength_forHEAD() throws IOException {
        String expectedContentLength = "14";

        Response response = requestProcessor.processRequest(HEADRequest);
        String contentLength = response.getHeader("Content-Length");

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returnsContentType_forHEAD() throws IOException {
        String expectedContentType = "text/plain";

        Response response = requestProcessor.processRequest(HEADRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void processRequest_returns200_forAValidRequest_forGET() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(GETRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsContentLength_forGET() throws IOException {
        String expectedContentLength = "14";

        Response response = requestProcessor.processRequest(GETRequest);
        String contentLength = response.getHeader("Content-Length");

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returnsContentType_forGET() throws IOException {
        String expectedContentType = "text/plain";

        Response response = requestProcessor.processRequest(GETRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void processRequest_returnsFileContents_forGET() throws IOException {
        byte[] expectedBody = "file1 contents".getBytes();

        Response response = requestProcessor.processRequest(GETRequest);
        byte[] body = response.getBody();

        assertArrayEquals(expectedBody, body);
    }

}
