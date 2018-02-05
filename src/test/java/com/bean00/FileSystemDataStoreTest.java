package com.bean00;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemDataStoreTest {
    private static final String PATH_TO_ROOT = "/Users/jonchin/8th-Light/projects/java-server/cob_spec/public";
    private FileSystemDataStore dataStore = new FileSystemDataStore(PATH_TO_ROOT);

    @Test
    public void dataCanBeFound_returnsTrue_whenTheDataCanBeFound() {
        boolean dataCanBeFound = dataStore.dataCanBeFound("/file1");

        assertTrue(dataCanBeFound);
    }

    @Test
    public void dataCanBeFound_returnsFalse_whenTheFileDoesNotExist() {
        boolean dataCanBeFound = dataStore.dataCanBeFound("/foobar");

        assertFalse(dataCanBeFound);
    }

    @Test
    public void dataCanBeFound_returnsFalse_whenTheFileIsADirectory() {
        boolean dataCanBeFound = dataStore.dataCanBeFound("/directory");

        assertFalse(dataCanBeFound);
    }

    @Test
    public void getData_returnsTheCorrectData_forAFileWithContent() throws IOException {
        byte[] expectedData = "file1 contents".getBytes();

        byte[] data = dataStore.getData("/file1");

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void getData_returnsNoData_forAnEmptyFile() throws IOException {
        byte[] expectedData = new byte[0];

        byte[] data = dataStore.getData("/no-content");

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forATextFile() throws IOException {
        String expectedMediaType = "text/plain";

        String mediaType = dataStore.getMediaType("/text-file.txt");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forAnImageFile() throws IOException {
        String expectedMediaType = "image/jpeg";

        String mediaType = dataStore.getMediaType("/image.jpeg");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheCorrectMediaType_forAnUnknownFile() throws IOException {
        String expectedMediaType = "application/octet-stream";

        String mediaType = dataStore.getMediaType("/executable");

        assertEquals(expectedMediaType, mediaType);
    }

    @Test
    public void getMediaType_returnsTheMediaType_forAFileWithoutAnExtension() throws IOException {
        String expectedMediaType = "text/plain";

        String mediaType = dataStore.getMediaType("/file1");

        assertEquals(expectedMediaType, mediaType);
    }

}
