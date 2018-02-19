package com.bean00;

public class ServerOptions {
    private int port;
    private String directory;

    public ServerOptions(int port, String directory) {
        this.port = port;
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public int getPort() {
        return port;
    }

}
