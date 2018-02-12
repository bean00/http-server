package com.bean00;

public class ArgParser {

    public ServerOptions buildServerOptions(String[] args) {
        String directory = null;

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-d")) {
                    directory = args[i + 1];
                    i++;
                }
            }
        } catch (IndexOutOfBoundsException e) {

        }

        if (directory == null) {
            throw new IllegalArgumentException();
        }

        return new ServerOptions(directory);
    }

}
