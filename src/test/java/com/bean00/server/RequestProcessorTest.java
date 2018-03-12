package com.bean00.server;

import com.bean00.controllers.CobSpecController;
import com.bean00.datastore.DataStore;
import com.bean00.httpmessages.Method;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestProcessorTest {
    private DataStore dataStore;
    private RequestProcessor requestProcessor;
    private CobSpecController cobSpecController;

    @BeforeEach
    public void setup() throws IOException {
        dataStore = mock(DataStore.class);
        cobSpecController = new CobSpecController(dataStore);
        requestProcessor = new RequestProcessor();

        requestProcessor.addRoute(Method.GET, "/existing", cobSpecController::handleGetAndHeadRequests);

        when(dataStore.resourceExists("/existing")).thenReturn(true);
        when(dataStore.getResource("/existing")).thenReturn("".getBytes());
    }

    @Test
    public void processRequest_returns200_forGET() throws Throwable {
        int expectedStatusCode = 200;
        Request request = new Request("GET", "/existing");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns404_ifTheResourceDoesNotExist() throws Throwable {
        int expectedStatusCode = 404;
        Request request = new Request("GET", "/non-existing");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returns405_ifMethodIsNotAllowed() throws Throwable {
        int expectedStatusCode = 405;
        Request request = new Request("x", "/existing");

        Response response = requestProcessor.processRequest(request);
        int statusCode = response.getStatusCode();

        assertEquals(expectedStatusCode, statusCode);
    }

    @Test
    public void processRequest_returnsTheAllowedMethods_ifMethodIsNotAllowed() throws Throwable {
        String expectedAllowedMethods = "GET, HEAD, PUT";
        Request request = new Request("x", "/");
        RequestProcessor requestProcessor = new RequestProcessor();
        requestProcessor.addRoute(Method.GET, "*", (requestParam) -> null);
        requestProcessor.addRoute(Method.HEAD, "*", (requestParam) -> null);
        requestProcessor.addRoute(Method.PUT, "*", (requestParam) -> null);

        Response response = requestProcessor.processRequest(request);
        String allowedMethods = response.getHeader("Allow");

        assertEquals(expectedAllowedMethods, allowedMethods);
    }

    @Test
    public void processRequest_returnsTheAllowedMethods_ifNotAllURLsMatch() throws Throwable {
        String expectedAllowedMethods = "GET, HEAD";
        Request request = new Request("PUT", "/file");
        RequestProcessor requestProcessor = new RequestProcessor();
        requestProcessor.addRoute(Method.GET, "/file", (requestParam) -> null);
        requestProcessor.addRoute(Method.HEAD, "/file", (requestParam) -> null);
        requestProcessor.addRoute(Method.PUT, "/x", (requestParam) -> null);

        Response response = requestProcessor.processRequest(request);
        String allowedMethods = response.getHeader("Allow");

        assertEquals(expectedAllowedMethods, allowedMethods);
    }

    @Test
    public void processRequest_returnsTheAllowedMethods_ifThereAreDuplicateMethods() throws Throwable {
        String expectedAllowedMethods = "GET, HEAD";
        Request request = new Request("PUT", "/file");
        RequestProcessor requestProcessor = new RequestProcessor();
        requestProcessor.addRoute(Method.GET, "/file", (requestParam) -> null);
        requestProcessor.addRoute(Method.GET, "*", (requestParam) -> null);
        requestProcessor.addRoute(Method.HEAD, "/file", (requestParam) -> null);
        requestProcessor.addRoute(Method.HEAD, "*", (requestParam) -> null);

        Response response = requestProcessor.processRequest(request);
        String allowedMethods = response.getHeader("Allow");

        assertEquals(expectedAllowedMethods, allowedMethods);
    }

}
