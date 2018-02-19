package com.bean00;

import com.bean00.datastore.DataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArgParserTest {
    private DataStore dataStore;
    private ArgParser argParser;

    @BeforeEach
    public void setup() {
        dataStore = mock(DataStore.class);
        argParser = new ArgParser(dataStore);
        when(dataStore.directoryExists("path")).thenReturn(true);
        when(dataStore.directoryExists("path2")).thenReturn(true);
    }

    @Test
    public void buildServerOptions_buildsOptionsCorrectly_ifDirectoryIsPassedIn() {
        String expectedDirectory = "path";
        String[] args = {"-d", "path"};

        ServerOptions options = argParser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_buildsOptionsCorrectly_ifThereAreInValidFlags() {
        String expectedDirectory = "path";
        String[] args = {"_", "_", "-d", "path", "_", "_"};

        ServerOptions options = argParser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_returnsTheSecondDirectory_ifMoreThanOneIsPassedIn() {
        String expectedDirectory = "path2";
        String[] args = {"-d", "path", "-d", "path2"};

        ServerOptions options = argParser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_throwsAnException_ifTheDirectoryPassedInDoesNotExist() {
        String[] args = {"-d", "xx"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnEx_ifAValidDirectory_isFollowedByAnInvalidDirectory() {
        String[] args = {"-d", "path", "-d", "-p", "1"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifTheDirectoryFlagIsNotFollowedByAValue() {
        String[] args = {"_", "-d"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnEx_ifAValidDir_isFollowedByADirFlagWithoutAnyValue() {
        String[] args = {"-d", "path", "-p", "1000", "-d"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereAreNotAnyValidFlags() {
        String[] args = {"_", "_"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereIsOnlyOneArgument_andItIsNotAValidFlag() {
        String[] args = {"_"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereAreZeroArguments() {
        String[] args = {};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_returnsADefaultPort_ifNoPortIsPassedIn() {
        int expectedPort = 5000;
        String[] args = {"-d", "path"};

        ServerOptions options = argParser.buildServerOptions(args);
        int port = options.getPort();

        assertEquals(expectedPort, port);
    }

    @Test
    public void buildServerOptions_returnsThePort_ifItIsPassedInBeforeTheDirectory() {
        int expectedPort = 1;
        String[] args = {"-p", "1", "-d", "path"};

        ServerOptions options = argParser.buildServerOptions(args);
        int port = options.getPort();

        assertEquals(expectedPort, port);
    }

    @Test
    public void buildServerOptions_returnsThePort_ifItIsPassedInAfterTheDirectory() {
        int expectedPort = 1;
        String[] args = {"-d", "path", "-p", "1"};

        ServerOptions options = argParser.buildServerOptions(args);
        int port = options.getPort();

        assertEquals(expectedPort, port);
    }

    @Test
    public void buildServerOptions_returnsTheSecondPort_ifMoreThanOneIsPassedIn() {
        int expectedPort = 2;
        String[] args = {"-p", "1", "-p", "2", "-d", "path"};

        ServerOptions options = argParser.buildServerOptions(args);
        int port = options.getPort();

        assertEquals(expectedPort, port);
    }

    @Test
    public void buildServerOptions_throwsAnException_ifAnInvalidArgumentIsBetweenThePortArgs() {
        String[] args = {"-p", "_", "1", "-d", "path"};

        assertThrows(IllegalArgumentException.class, () -> argParser.buildServerOptions(args));
    }

}
