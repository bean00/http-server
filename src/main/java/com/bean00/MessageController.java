package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MessageController {
    private BufferedReader in;
    private PrintWriter out;

    public MessageController(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void readRequest() throws IOException {
        String inputLine;

        in.readLine();

        do {
            inputLine = in.readLine();
        } while (inputLine != null && !inputLine.isEmpty());
    }

    public void writeResponse() {
        Response response = new Response();

        out.println(response.simple200Response());
    }

}
