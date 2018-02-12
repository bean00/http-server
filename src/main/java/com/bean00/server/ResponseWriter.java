package com.bean00.server;

import com.bean00.response.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    private OutputStream outputStream;

    public ResponseWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeResponse(Response response) throws IOException {
        byte[] responseAsByteArray = response.getResponseAsByteArray();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(responseAsByteArray);
        byteArrayOutputStream.writeTo(outputStream);
    }

}
