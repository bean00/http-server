package com.bean00;

import com.bean00.httpmessages.Request;
import com.bean00.httpmessages.Response;
import com.bean00.server.RequestParser;
import com.bean00.server.RequestProcessor;
import com.bean00.server.ResponseWriter;
import com.bean00.server.Server;
import io.vavr.CheckedFunction1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Application {
    private Server server = new Server();
    private ServerSocket serverSocket;
    private RequestProcessor requestProcessor = new RequestProcessor();

    public void setup(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
    }

    public void addRoute(String routeMethod, String routeURL, CheckedFunction1<Request, Response> controller) {
        requestProcessor.addRoute(routeMethod, routeURL, controller);
    }

    public void run() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream();

            RequestParser requestParser = new RequestParser(reader);
            ResponseWriter responseWriter = new ResponseWriter(outputStream);

            server.run(requestParser, requestProcessor, responseWriter);

            outputStream.close();
            reader.close();
            clientSocket.close();
        }
    }

}
