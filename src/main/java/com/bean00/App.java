package com.bean00;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        System.out.println( "Server started" );

        // create a socket
        ServerSocket serverSocket =
                new ServerSocket(5000);
        Socket clientSocket = serverSocket.accept();
        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        // receive the request
        String inputLine;

        while (true) {
            for (int i = 0; i < 6; i++) {
                inputLine = in.readLine();
                System.out.println(inputLine);
            }

            // returns a response
            out.println("HTTP/1.1 200 OK\r");

            break;
        }

    }
}
