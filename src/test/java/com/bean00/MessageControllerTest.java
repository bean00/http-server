package com.bean00;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessageControllerTest {

    @Test
    public void readRequest_readsTheRequest() throws IOException {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        PrintWriter out = Mockito.mock(PrintWriter.class);
        Mockito.when(in.readLine())
                .thenReturn("line1", "line2", "line3", "");
        MessageController messageController = new MessageController(in, out);

        messageController.readRequest();

        verify(in, times(4)).readLine();
    }

    @Test
    public void writeResponse_writesASimple200Response() {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);
        String simple200Response = "HTTP/1.1 200 OK\r\n";
        MessageController messageController = new MessageController(in, out);

        messageController.writeResponse();
        String response = stringWriter.toString();

        assertEquals(simple200Response, response);
    }

}
