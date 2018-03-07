package com.bean00.server;

import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.HttpHeaders;
import com.bean00.datastore.DataStore;
import com.bean00.httpmessages.Method;
import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import com.bean00.httpmessages.Status;

import java.io.IOException;

public class RequestProcessor {
    private DataStore dataStore;

    public RequestProcessor(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Response processRequest(Request request) throws IOException {
        String requestMethod = request.getRequestMethod();
        Response response;

        if (requestMethod.equals(Method.PUT)) {
            response = handlePutRequest(request);
        } else if (requestMethod.equals(Method.GET) || requestMethod.equals(Method.HEAD)) {
            response = handleGetAndHeadRequests(request);
        } else if (requestMethod.equals(Method.DELETE)) {
            response = handleDeleteRequest(request);
        } else {
            String[] allowedMethods = {Method.GET, Method.HEAD, Method.PUT};
            response = buildMethodNotAllowedResponse(allowedMethods);
        }

        return response;
    }

    private Response handlePutRequest(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        byte[] requestBody = request.getBodyAsBytes();

        if (dataStore.isDirectory(requestURL)) {
            String[] allowedMethods = {Method.GET, Method.HEAD};
            return buildMethodNotAllowedResponse(allowedMethods);
        }

        int statusCode = dataStore.resourceExists(requestURL) ? Status.OK : Status.CREATED;

        dataStore.put(requestURL, requestBody);

        return new Response(statusCode);
    }

    private Response buildMethodNotAllowedResponse(String[] allowedMethods) {
        String allowedMethodsString = buildAllowedMethodsString(allowedMethods);
        String[][] rawHeaders = {{"Allow", allowedMethodsString}};
        HttpHeaders headers = new HttpHeaders(rawHeaders);

        return new Response(Status.METHOD_NOT_ALLOWED, headers);
    }

    private String buildAllowedMethodsString(String[] allowedMethods) {
        return String.join(", ", allowedMethods);
    }

    private Response handleGetAndHeadRequests(Request request) throws IOException {
        String requestMethod = request.getRequestMethod();
        String requestURL = request.getRequestURL();

        if (!dataStore.resourceExists(requestURL)) {
            return new Response(Status.NOT_FOUND);
        }

        byte[] body = dataStore.getResource(requestURL);
        HttpHeaders headers = setContentLengthAndType(body, requestURL);

        return new Response(Status.OK, requestMethod, headers, body);
    }

    private HttpHeaders setContentLengthAndType(byte[] body, String requestURL) throws IOException {
        HttpHeaders headers = new HttpHeaders();

        String contentLength = Integer.toString(body.length);
        String contentType = dataStore.getMediaType(requestURL);

        headers.setHeader("Content-Length", contentLength);
        headers.setHeader("Content-Type", contentType);

        return headers;
    }

    private Response handleDeleteRequest(Request request) throws IOException {
        String requestURL = request.getRequestURL();
        int statusCode;

        if (dataStore.isDirectoryWithContent(requestURL)) {
            String errorMessage = "[ERROR] Trying to delete a directory that has contents.\n";
            throw new BadRequestHttpException(errorMessage);
        }

        if (!dataStore.resourceExists(requestURL)) {
            statusCode = Status.NO_CONTENT;
        } else {
            dataStore.delete(requestURL);
            statusCode = Status.OK;
        }

        return new Response(statusCode);
    }

}
