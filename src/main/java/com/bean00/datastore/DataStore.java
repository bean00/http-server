package com.bean00.datastore;

import java.io.IOException;

public interface DataStore {

    boolean isDirectory(String url);

    boolean resourceExists(String url);

    byte[] getResource(String url) throws IOException;

    String getMediaType(String url) throws IOException;

    void put(String url, byte[] fileContents) throws IOException;

}
