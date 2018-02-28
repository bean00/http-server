package com.bean00.server;

import com.bean00.httpexception.BadRequestHttpException;
import com.bean00.httpmessages.Request;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RequestParserTest {

    @Test
    public void parseRequest_callsReadLineTheCorrectNumberOfTimes() throws IOException {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        Mockito.when(in.readLine()).thenReturn(
                "GET / HTTP/1.1",
                "Host: localhost:5000",
                "Connection: Keep-Alive",
                "");
        RequestParser parser = new RequestParser(in);

        parser.parseRequest();

        verify(in, times(4)).readLine();
    }

    @Test
    public void parseRequest_createsARequest_thatHasARequestMethod() throws IOException {
        String expectedRequestMethod = "GET";
        String getRequest = "GET / HTTP/1.1";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String requestMethod = request.getRequestMethod();

        assertEquals(expectedRequestMethod, requestMethod);
    }

    @Test
    public void parseRequest_createsARequest_thatHasARequestURL() throws IOException {
        String expectedRequestURL = "/";
        String getRequest = "GET / HTTP/1.1";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

    @Test
    public void parseRequest_createsARequest_thatHasAFieldValue_forAHeader() throws IOException {
        String expectedFieldValue = "localhost:5000";
        String getRequest =
                "GET / HTTP/1.1\r\n" +
                "Host: localhost:5000\r\n" +
                "\r\n";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String fieldValue = request.getHeader("Host");

        assertEquals(expectedFieldValue, fieldValue);
    }

    @Test
    public void parseRequest_createsARequest_thatHasABody() throws IOException {
        String expectedBody = "<p>New File</p>\n";
        String putRequest =
                "PUT /new.html HTTP/1.1\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 16\r\n" +
                "\r\n" +
                "<p>New File</p>\n";
        RequestParser parser = createRequestParser(putRequest);

        Request request = parser.parseRequest();
        String body = request.getBody();

        assertEquals(expectedBody, body);
    }

    @Test
    public void parseRequest_createsARequest_thatDoesNotHaveABody() throws IOException {
        String expectedBody = "";
        String getRequest =
                "GET / HTTP/1.1\r\n" +
                "\r\n";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String body = request.getBody();

        assertEquals(expectedBody, body);
    }

    @Test
    public void parseRequest_createsARequest_thatHasAnEmptyBody_ifContentLengthIs0() throws IOException {
        String expectedBody = "";
        String getRequest =
                "PUT /new HTTP/1.1\r\n" +
                "Content-Length: 0\r\n";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String body = request.getBody();

        assertEquals(expectedBody, body);
    }

    @Test
    public void parseRequest_handlesAnySpaces_beforeTheRequestLine() throws IOException {
        String expectedRequestURL = "/";
        String getRequest = "    GET / HTTP/1.1";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

    @Test
    public void parseRequest_handlesAnySpaces_betweenWordsInTheRequestLine() throws IOException {
        String expectedRequestURL = "/";
        String getRequest = "GET     /   HTTP/1.1";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

    @Test
    public void parseRequest_handlesAnySpaces_afterTheRequestLine() throws IOException {
        String expectedRequestURL = "/";
        String getRequest = "GET / HTTP/1.1    ";
        RequestParser parser = createRequestParser(getRequest);

        Request request = parser.parseRequest();
        String requestURL = request.getRequestURL();

        assertEquals(expectedRequestURL, requestURL);
    }

    @Test
    public void parseRequest_throwsBadRequestException_ifReadLineReturnsNull_forRequestLine() throws IOException {
        BufferedReader in = Mockito.mock(BufferedReader.class);
        Mockito.when(in.readLine()).thenReturn(null);
        RequestParser parser = new RequestParser(in);

        assertThrows(BadRequestHttpException.class, () -> parser.parseRequest());
    }

    @Test
    public void parseRequest_throwsBadRequestException_ifRequestLineDoesNotHave_3_words() {
        String getRequest = "1 2 3 4";
        RequestParser parser = createRequestParser(getRequest);

        assertThrows(BadRequestHttpException.class, () -> parser.parseRequest());
    }

    @Test
    public void parseRequest_throwsBadRequestException_ifHttpVersionIsNot_1_1() {
        String getRequest = "GET / HTTP/1.0";
        RequestParser parser = createRequestParser(getRequest);

        assertThrows(BadRequestHttpException.class, () -> parser.parseRequest());
    }

    @Test
    public void parseRequest_throwsBadRequestException_ifFieldNameHasLeadingSpaces() {
        String putRequest =
                "PUT /new HTTP/1.1\r\n" +
                "   Content-Length: 3\r\n";
        RequestParser parser = createRequestParser(putRequest);

        assertThrows(BadRequestHttpException.class, () -> parser.parseRequest());
    }

    @Test
    public void parseRequest_throwsBadRequestException_ifFieldNameHasTrailingSpaces() {
        String putRequest =
                "PUT /new HTTP/1.1\r\n" +
                "Content-Length   : 3\r\n";
        RequestParser parser = createRequestParser(putRequest);

        assertThrows(BadRequestHttpException.class, () -> parser.parseRequest());
    }

    private RequestParser createRequestParser(String request) {
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        RequestParser parser = new RequestParser(in);

        return parser;
    }

}
