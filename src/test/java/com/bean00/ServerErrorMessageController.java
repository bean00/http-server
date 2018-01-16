package com.bean00;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ServerErrorMessageController extends MessageController {

    public ServerErrorMessageController(BufferedReader in, PrintWriter out) {
        super(in, out);
    }

    @Override
    public Response interpretRequest(Request request) {
        throw new RuntimeException();
    }

}
