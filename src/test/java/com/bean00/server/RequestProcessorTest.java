package com.bean00.server;

import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.HttpHeaders;
import com.bean00.datastore.DataStore;
import com.bean00.datastore.FileSystemDataStore;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestProcessorTest {
    private Request fileGETRequest = new Request("GET", "/file1");
    private Request fileHEADRequest = new Request("HEAD", "/file1");
    private Request directoryGETRequest = new Request("GET", "/directory");
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
        when(dataStore.isDirectory("/directory")).thenReturn(true);
        when(dataStore.resourceExists("/directory")).thenReturn(true);
        when(dataStore.getResource("/directory")).thenReturn(body);
        when(dataStore.getMediaType("/directory")).thenReturn("text/html; charset=utf-8");
        when(dataStore.isDirectoryWithContent("/directory")).thenReturn(true);

        when(dataStore.resourceExists("/existing")).thenReturn(true);
    }

    @Test
    public void processRequest_returns200_forGETRequest() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(fileGETRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns200_forGETRequest_toTheRootDirectory() throws IOException {
        int expectedStatusCode = 200;
        Request rootRequest = new Request("GET", "/");
        when(dataStore.resourceExists("/")).thenReturn(true);
        when(dataStore.getResource("/")).thenReturn(new byte[0]);

        Response response = requestProcessor.processRequest(rootRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns200_forHEADRequest() throws IOException {
        int expectedStatusCode = 200;

        Response response = requestProcessor.processRequest(fileHEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns200_forPUTRequest_whenTheFileExists() throws IOException {
        int expectedStatusCode = 200;
        Request request = new Request("PUT", "/existing");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsOnlyTheRequestLine_forPUTRequest() throws IOException {
        String expectedResponseString =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Request request = new Request("PUT", "/existing");

        Response response = requestProcessor.processRequest(request);
        String responseString = response.toString();

        assertEquals(expectedResponseString, responseString);
    }

    @Test
    public void processRequest_rewritesTheFile_forPUTRequest_whenTheFileExists() throws IOException {
        String fileContents = "NEW contents";
        byte[] fileContentsAsBytes = fileContents.getBytes();
        Request request = new Request("PUT", "/existing", new HttpHeaders(), fileContents);

        requestProcessor.processRequest(request);

        verify(dataStore, times(1)).put("/existing", fileContentsAsBytes);
    }

    @Test
    public void processRequest_createsAndWritesToAFile_forPUT_whenTheFileDoesNotExist() throws IOException {
        String fileContents = "contents";
        byte[] fileContentsAsBytes = fileContents.getBytes();
        Request request = new Request("PUT", "/non-existing", new HttpHeaders(), fileContents);

        requestProcessor.processRequest(request);

        verify(dataStore, times(1)).put("/non-existing", fileContentsAsBytes);
    }

    @Test
    public void processRequest_returns201_forPUTRequest_whenAFileIsCreated() throws IOException {
        // This test needs to hit the filesystem
        // - Need to start with a file that doesn't exist, create it, and
        //   confirm that it was created (Mocks won't work for this)
        int expectedStatusCode = 201;
        String pathToTestFiles = "src/test/resources/test-files";
        FileSystemDataStore dataStore = new FileSystemDataStore(pathToTestFiles);
        Request request = new Request("PUT", "/non-existing");
        RequestProcessor requestProcessor = new RequestProcessor(dataStore);

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        // Need to delete /non-existing, because processRequest creates this file
        Files.delete(Paths.get(pathToTestFiles, "/non-existing"));

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns405_forPUTRequest_toADirectory() throws IOException {
        int expectedStatusCode = 405;
        Request request = new Request("PUT", "/directory");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsTheAllowHeader_forPUTRequest_toADirectory() throws IOException {
        String expectedAllowHeader = "GET, HEAD";
        Request request = new Request("PUT", "/directory");

        Response response = requestProcessor.processRequest(request);
        String allowHeader = response.getHeader("Allow");

        assertEquals(expectedAllowHeader, allowHeader);
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
        String bodyAsString = response.toString();

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void processRequest_returnsA404Response_ifTheResourceDoesNotExist() throws IOException {
        int expectedStatusCode = 404;
        Request request = new Request("GET", "/non-existing");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsA405Response_ifMethodIsNotFound() throws IOException {
        int expectedStatusCode = 405;
        Request request = new Request("x", "/");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsTheAllowHeader_ifMethodIsNotFound() throws IOException {
        String expectedAllowHeader = "GET, HEAD, PUT";
        Request request = new Request("x", "/");

        Response response = requestProcessor.processRequest(request);
        String allowHeader = response.getHeader("Allow");

        assertEquals(expectedAllowHeader, allowHeader);
    }

    @Test
    public void processRequest_returnsA200Response_ifTheFileIsDeleted_forDELETE() throws IOException {
        int expectedStatusCode = 200;
        Request request = new Request("DELETE", "/file1");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns204_ifDeleting_aResourceThatDoesNotExist_forDELETE() throws IOException {
        int expectedStatusCode = 204;
        Request request = new Request("DELETE", "/non-existing");
        when(dataStore.resourceExists("/non-existing")).thenReturn(false);

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_deletesTheFile_forDELETE() throws IOException {
        Request request = new Request("DELETE", "/file1");

        requestProcessor.processRequest(request);

        verify(dataStore, times(1)).delete("/file1");
    }

    @Test
    public void processRequest_throwsABadRequestException_forADirectoryWithContents_forDELETE() {
        Request request = new Request("DELETE", "/directory");

        assertThrows(BadRequestHttpException.class, () -> requestProcessor.processRequest(request));
    }

    @Test
    public void processRequest_throwsAnException_thatIncludesAMessage_forDeletingADirectoryWithContents() {
        String expectedErrorMessage = "[ERROR] Trying to delete a directory that has contents.\n";
        Request request = new Request("DELETE", "/empty-directory");
        when(dataStore.isDirectoryWithContent("/empty-directory")).thenReturn(true);

        Throwable exception = assertThrows(BadRequestHttpException.class,
                () -> requestProcessor.processRequest(request));

        assertEquals(expectedErrorMessage, exception.getMessage());
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
