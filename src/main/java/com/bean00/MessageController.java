package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageController {
    private RequestParser parser;
    private PrintWriter out;
    private RequestInterpreter interpreter = new RequestInterpreter();

    public MessageController(BufferedReader in, PrintWriter out) {
        parser = new RequestParser(in);

        this.out = out;
    }

    public Request getRequest() throws IOException {
        Request request = parser.parseRequest();

        return request;
    }

    public Response interpretRequest(Request request) {
        String requestURL = request.getRequestURL();

        // *Note: shouldn't be choosing status code based on the requestURL
        int statusCode = interpreter.chooseStatusCode(requestURL);
        Response response = new Response(statusCode);

        return response;
    }

    public void writeResponse(Response response) {
        String responseString = response.toString();

        out.print(responseString);
        out.flush();
    }

}
