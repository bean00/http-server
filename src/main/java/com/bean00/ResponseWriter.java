package com.bean00;

import java.io.IOException;
import java.io.Writer;

public class ResponseWriter {
    private Writer out;

    public ResponseWriter(Writer out) {
        this.out = out;
    }

    public void writeResponse(Response response) throws IOException {
        String responseString = response.toString();

        out.write(responseString);
        out.flush();
    }

}
