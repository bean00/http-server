package com.bean00.server;

import com.bean00.datastore.DataStore;
import com.bean00.httpexception.NotFoundHttpException;
import com.bean00.request.Request;
import com.bean00.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestProcessorTest {
    private Request fileGETRequest = new Request("GET", "/file1", new ArrayList<>());
    private Request fileHEADRequest = new Request("HEAD", "/file1", new ArrayList<>());
    private Request directoryGETRequest = new Request("GET", "/directory", new ArrayList<>());
    private DataStore dataStore;
    private RequestProcessor requestProcessor;

    @BeforeEach
    public void setup() throws IOException {
        dataStore = mock(DataStore.class);
        requestProcessor = new RequestProcessor(dataStore);

        when(dataStore.resourceExists("/file1")).thenReturn(true);
        when(dataStore.getResource("/file1")).thenReturn("file1 contents".getBytes());
        when(dataStore.getMediaType("/file1")).thenReturn("text/plain");

        byte[] body = buildBody();
        when(dataStore.resourceExists("/directory")).thenReturn(true);
        when(dataStore.getResource("/directory")).thenReturn(body);
        when(dataStore.getMediaType("/directory")).thenReturn("text/html; charset=utf-8");
    }

    @Test
    public void processRequest_returns200_forAValidRequest_toTheRootDirectory() throws IOException {
        int expectedStatusCode = 200;
        Request rootRequest = new Request("GET", "/", new ArrayList<>());
        when(dataStore.resourceExists("/")).thenReturn(true);
        when(dataStore.getResource("/")).thenReturn(new byte[0]);

        Response response = requestProcessor.processRequest(rootRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns200_forAValidRequest_forHEAD() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(fileHEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsContentLength_forHEAD() throws IOException {
        int expectedContentLength = 14;

        Response response = requestProcessor.processRequest(fileHEADRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returnsContentType_forHEAD() throws IOException {
        String expectedContentType = "text/plain";

        Response response = requestProcessor.processRequest(fileHEADRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void processRequest_returns200_forAValidRequest_forGET() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(fileGETRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsContentLength_forGET() throws IOException {
        int expectedContentLength = 14;

        Response response = requestProcessor.processRequest(fileGETRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returnsContentType_forGET() throws IOException {
        String expectedContentType = "text/plain";

        Response response = requestProcessor.processRequest(fileGETRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void processRequest_returnsFileContents_forGET() throws IOException {
        byte[] expectedBody = "file1 contents".getBytes();

        Response response = requestProcessor.processRequest(fileGETRequest);
        byte[] body = response.getBody();

        assertArrayEquals(expectedBody, body);
    }

    @Test
    public void processRequest_returnsContentType_forDirectoryListing() throws IOException {
        String expectedContentType = "text/html; charset=utf-8";

        Response response = requestProcessor.processRequest(directoryGETRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void processRequest_returnsContentLength_forDirectoryListing() throws IOException {
        int expectedContentLength = 124;

        Response response = requestProcessor.processRequest(directoryGETRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void processRequest_returnsHTML_thatHasTheSortedList_ofFiles() throws IOException {
        String expectedHTML =
                "<ul>\n" +
                "<li>file1</li>\n" +
                "<li>file2</li>\n" +
                "</ul>\n";

        Response response = requestProcessor.processRequest(directoryGETRequest);
        String bodyAsString = response.getResponseAsString();

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void processRequest_throwsNotFoundException_ifTheResourceDoesNotExist() {
        Request notFoundGETRequest = new Request("GET", "/foobar", new ArrayList<>());
        when(dataStore.resourceExists("/foobar")).thenReturn(false);

        assertThrows(NotFoundHttpException.class, () -> requestProcessor.processRequest(notFoundGETRequest));
    }

    private byte[] buildBody() {
        String htmlBody =
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Server</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<ul>\n" +
                "<li>file1</li>\n" +
                "<li>file2</li>\n" +
                "</ul>\n" +
                "</body>\n" +
                "</html>\n";

        return htmlBody.getBytes();
    }

    private int getContentLength(Response response) {
        String contentLengthAsString = response.getHeader("Content-Length");
        int contentLength = Integer.parseInt(contentLengthAsString);

        return contentLength;
    }

}
