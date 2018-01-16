package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Driver {

    public static void main(String[] args) throws IOException {
        int portNumber = 5000;
        System.out.println("Server started on port " + portNumber);

        ServerSocket serverSocket = new ServerSocket(portNumber);
        Server server = new Server();
        MessageController messageController = new MessageController();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            server.run(messageController, in, out);

            in.close();
            out.close();
            clientSocket.close();
        }
    }

}
