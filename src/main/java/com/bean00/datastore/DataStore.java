package com.bean00.datastore;

import java.io.IOException;

public interface DataStore {

    boolean isDirectory(String url);

    boolean resourceExists(String url);

    boolean isDirectoryWithContent(String requestURL);

    byte[] getResource(String url) throws IOException;

    String getMediaType(String url) throws IOException;

    void put(String url, byte[] fileContents) throws IOException;

    void delete(String url) throws IOException;

}
