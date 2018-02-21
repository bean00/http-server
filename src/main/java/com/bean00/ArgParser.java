package com.bean00;

import com.bean00.datastore.DataStore;

public class ArgParser {
    private DataStore dataStore;

    public ArgParser(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public ServerOptions buildServerOptions(String[] args) {
        String directory = null;
        int port = 5000;

        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-p")) {
                    String portAsString = args[i + 1];
                    port = Integer.parseInt(portAsString);
                    i++;
                } else if (args[i].equals("-d")) {
                    if (!dataStore.resourceExists(args[i + 1])) {
                        throw new IllegalArgumentException();
                    }

                    directory = args[i + 1];
                    i++;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        if (directory == null) {
            throw new IllegalArgumentException();
        }

        return new ServerOptions(port, directory);
    }

}
