package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MessageController {
    private RequestReader reader;
    private PrintWriter out;
    private RequestParser parser = new RequestParser();
    private RequestInterpreter interpreter = new RequestInterpreter();

    public MessageController(BufferedReader in, PrintWriter out) {
        reader = new RequestReader(in);

        this.out = out;
    }

    public Request readRequest() throws IOException {
        String requestLine = reader.getRequestLine();
        ArrayList<String> headers = reader.getHeaders();

        Request request = new Request(requestLine, headers);

        return request;
    }

    public Response interpretRequest(Request request) {
        String target = request.getTarget();
        int statusCode = interpreter.chooseStatusCode(target);
        Response response = new Response(statusCode);

        return response;
    }

    public void writeResponse(Response response) {
        String responseString = response.toString();

        out.println(responseString);
    }

}
