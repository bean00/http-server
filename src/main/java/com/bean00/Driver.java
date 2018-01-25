package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver {

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        int portNumber = 5000;

        ServerSocket serverSocket =
                new ServerSocket(portNumber);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        Server server = new Server(in, out);
        server.handleGET();
    }

}
