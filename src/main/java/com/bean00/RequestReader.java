package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class RequestReader {
    private BufferedReader in;

    public RequestReader(BufferedReader in) {
        this.in = in;
    }

    public String getRequestLine() throws IOException {
        return in.readLine();
    }

    public ArrayList<String> getHeaders() throws IOException {
        ArrayList<String> headers = new ArrayList<>();
        String header;

        do {
            header = in.readLine();

            if (isLineBlank(header)) {
                break;
            }

            headers.add(header);
        } while (header != null);

        return headers;
    }

    private boolean isLineBlank(String line) {
        return line.isEmpty();
    }

}
