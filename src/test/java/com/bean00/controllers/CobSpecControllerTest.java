package com.bean00.controllers;

import com.bean00.datastore.DataStore;
import com.bean00.datastore.FileSystemDataStore;
import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.HttpHeaders;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CobSpecControllerTest {
    private Request fileGETRequest = new Request("GET", "/file1");
    private Request directoryGETRequest = new Request("GET", "/directory");
    private Request fileHEADRequest = new Request("HEAD", "/file1");
    private DataStore dataStore;
    private CobSpecController cobSpecCtrl;

    @BeforeEach
    public void setup() throws IOException {
        dataStore = mock(DataStore.class);
        cobSpecCtrl = new CobSpecController(dataStore);

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
    public void handleGetAndHeadRequests_returns200_forGET() throws IOException {
        int expectedStatusCode = 200;

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileGETRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleGetAndHeadRequests_returns200_forGET_toTheRootDirectory() throws IOException {
        int expectedStatusCode = 200;
        Request rootRequest = new Request("GET", "/");
        when(dataStore.resourceExists("/")).thenReturn(true);
        when(dataStore.getResource("/")).thenReturn(new byte[0]);

        Response response = cobSpecCtrl.handleGetAndHeadRequests(rootRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentLength_forGET() throws IOException {
        int expectedContentLength = 14;

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileGETRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentType_forGET() throws IOException {
        String expectedContentType = "text/plain";

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileGETRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void handleGetAndHeadRequests_returnsFileContents_forGET() throws IOException {
        byte[] expectedBody = "file1 contents".getBytes();

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileGETRequest);
        byte[] body = response.getBody();

        assertArrayEquals(expectedBody, body);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentType_forGET_toDirectory() throws IOException {
        String expectedContentType = "text/html; charset=utf-8";

        Response response = cobSpecCtrl.handleGetAndHeadRequests(directoryGETRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentLength_forGET_toDirectory() throws IOException {
        int expectedContentLength = 124;

        Response response = cobSpecCtrl.handleGetAndHeadRequests(directoryGETRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void handleGetAndHeadRequests_returnsCorrectHTML_forGET_toDirectory() throws IOException {
        String expectedHTML =
                "<ul>\n" +
                "<li>file1</li>\n" +
                "<li>file2</li>\n" +
                "</ul>\n";

        Response response = cobSpecCtrl.handleGetAndHeadRequests(directoryGETRequest);
        String bodyAsString = response.toString();

        assertTrue(bodyAsString.contains(expectedHTML));
    }

    @Test
    public void handleGetAndHeadRequests_returns200_forHEAD() throws IOException {
        int expectedStatusCode = 200;

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileHEADRequest);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentLength_forHEAD() throws IOException {
        int expectedContentLength = 14;

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileHEADRequest);
        int contentLength = getContentLength(response);

        assertEquals(expectedContentLength, contentLength);
    }

    @Test
    public void handleGetAndHeadRequests_returnsContentType_forHEAD() throws IOException {
        String expectedContentType = "text/plain";

        Response response = cobSpecCtrl.handleGetAndHeadRequests(fileHEADRequest);
        String contentType = response.getHeader("Content-Type");

        assertEquals(expectedContentType, contentType);
    }

    @Test
    public void handlePutRequest_returns200_forPUT_whenTheFileExists() throws Throwable {
        int expectedStatusCode = 200;
        Request request = new Request("PUT", "/existing");

        Response response = cobSpecCtrl.handlePutRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handlePutRequest_returnsOnlyTheRequestLine_forPUT() throws IOException {
        String expectedResponseString =
                "HTTP/1.1 200 OK\r\n" +
                "\r\n";
        Request request = new Request("PUT", "/existing");

        Response response;
        response = cobSpecCtrl.handlePutRequest(request);
        String responseString = response.toString();

        assertEquals(expectedResponseString, responseString);
    }

    @Test
    public void handlePutRequest_rewritesTheFile_forPUT_whenTheFileExists() throws IOException {
        String fileContents = "NEW contents";
        byte[] fileContentsAsBytes = fileContents.getBytes();
        Request request = new Request("PUT", "/existing", new HttpHeaders(), fileContents);

        cobSpecCtrl.handlePutRequest(request);

        verify(dataStore, times(1)).put("/existing", fileContentsAsBytes);
    }

    @Test
    public void handlePutRequest_createsAndWritesToAFile_forPUT_whenTheFileDoesNotExist() throws IOException {
        String fileContents = "contents";
        byte[] fileContentsAsBytes = fileContents.getBytes();
        Request request = new Request("PUT", "/non-existing", new HttpHeaders(), fileContents);

        cobSpecCtrl.handlePutRequest(request);

        verify(dataStore, times(1)).put("/non-existing", fileContentsAsBytes);
    }

    @Test
    public void handlePutRequest_returns201_forPUT_whenAFileIsCreated() throws IOException {
        // This test needs to hit the filesystem
        // - Need to start with a file that doesn't exist, create it, and
        //   confirm that it was created (Mocks won't work for this)
        int expectedStatusCode = 201;
        String pathToTestFiles = "src/test/resources/test-files";
        FileSystemDataStore dataStore = new FileSystemDataStore(pathToTestFiles);
        Request request = new Request("PUT", "/non-existing");
        CobSpecController cobSpecCtrl = new CobSpecController(dataStore);

        Response response = cobSpecCtrl.handlePutRequest(request);
        int statusCode = response.getStatusCode();

        // Need to delete /non-existing
        Files.delete(Paths.get(pathToTestFiles, "/non-existing"));

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleCatFormPost_returns201_whenAResourceIsCreated_forPOST() throws IOException {
        int expectedStatusCode = 201;
        Request request = new Request("POST", "/cat-form");

        Response response = cobSpecCtrl.handleCatFormPost(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleCatFormPost_returnsALocationHeader_forPOST() throws IOException {
        String expectedLocation = "/cat-form/data";
        Request request = new Request("POST", "/cat-form");

        Response response = cobSpecCtrl.handleCatFormPost(request);
        String location = response.getHeader("Location");

        assertEquals(expectedLocation, location);
    }

    @Test
    public void handleCatFormPost_createsAResource_forPOST() throws IOException {
        // This test needs to hit the filesystem
        // - Need to create a /form directory, then a /form/data file
        String pathToTestFiles = "src/test/resources/test-files";
        FileSystemDataStore dataStore = new FileSystemDataStore(pathToTestFiles);
        String fileContents = "data=fatcat";
        Request request = new Request("POST", "/cat-form", new HttpHeaders(), fileContents);
        CobSpecController cobSpecCtrl = new CobSpecController(dataStore);

        cobSpecCtrl.handleCatFormPost(request);
        boolean resourceExists = dataStore.resourceExists("/cat-form/data");

        // Need to delete /form/data and /form
        Files.delete(Paths.get(pathToTestFiles, "/cat-form/data"));
        Files.delete(Paths.get(pathToTestFiles, "/cat-form"));

        assertTrue(resourceExists);
    }

    @Test
    public void handleCatFormPost_returns200_whenTheResourceAlreadyExists_forPOST() throws IOException {
        int expectedStatusCode = 200;
        Request request = new Request("POST", "/cat-form");
        when(dataStore.resourceExists("/cat-form")).thenReturn(true);

        Response response = cobSpecCtrl.handleCatFormPost(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleCatFormPost_rewritesTheFile_whenTheResourceAlreadyExists_forPOST() throws IOException {
        String fileContents = "data";
        byte[] fileContentsAsBytes = fileContents.getBytes();
        Request request = new Request("PUT", "/cat-form", new HttpHeaders(), fileContents);
        when(dataStore.resourceExists("/cat-form")).thenReturn(true);

        cobSpecCtrl.handleCatFormPost(request);

        verify(dataStore, times(1)).put("/cat-form/data", fileContentsAsBytes);
    }

    @Test
    public void handleFormPost_returns200_forPOST_toForm() {
        int expectedStatusCode = 200;
        Request request = new Request("POST", "/form");

        Response response = cobSpecCtrl.handleFormPost(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleDeleteRequest_returns200_forDeletingAFile_forDELETE() throws IOException {
        int expectedStatusCode = 200;
        Request request = new Request("DELETE", "/file1");

        Response response = cobSpecCtrl.handleDeleteRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleDeleteRequest_returns204_forDeletingAResourceThatDoesNotExist_forDELETE() throws IOException {
        int expectedStatusCode = 204;
        Request request = new Request("DELETE", "/non-existing");
        when(dataStore.resourceExists("/non-existing")).thenReturn(false);

        Response response = cobSpecCtrl.handleDeleteRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void handleDeleteRequest_deletesTheFile_forDELETE() throws IOException {
        Request request = new Request("DELETE", "/file1");

        cobSpecCtrl.handleDeleteRequest(request);

        verify(dataStore, times(1)).delete("/file1");
    }

    @Test
    public void handleDeleteRequest_throwsABadRequestException_forADirectoryWithContents_forDELETE() {
        Request request = new Request("DELETE", "/directory");

        assertThrows(BadRequestHttpException.class, () -> cobSpecCtrl.handleDeleteRequest(request));
    }

    @Test
    public void handleDeleteRequest_includesAMessageInTheException_forDELETE_forADirectoryWithContents() {
        String expectedErrorMessage = "[ERROR] Trying to delete a directory that has contents.\n";
        Request request = new Request("DELETE", "/empty-directory");
        when(dataStore.isDirectoryWithContent("/empty-directory")).thenReturn(true);

        Throwable exception = assertThrows(BadRequestHttpException.class,
                () -> cobSpecCtrl.handleDeleteRequest(request));

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
