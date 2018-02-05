package com.bean00;

import java.io.IOException;

public interface DataStore {

    boolean dataCanBeFound(String url);

    byte[] getData(String url) throws IOException;

    String getMediaType(String url) throws IOException;

}
