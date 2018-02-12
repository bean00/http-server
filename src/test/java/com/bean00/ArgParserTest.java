package com.bean00;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArgParserTest {

    @Test
    public void buildServerOptions_buildsOptionsCorrectly_ifDirectoryIsPassedIn() {
        String expectedDirectory = "path";
        String[] args = {"-d", "path"};
        ArgParser parser = new ArgParser();

        ServerOptions options = parser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_buildsOptionsCorrectly_ifThereAreInValidFlags() {
        String expectedDirectory = "path";
        String[] args = {"_", "_", "-d", "path", "_", "_"};
        ArgParser parser = new ArgParser();

        ServerOptions options = parser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_returnsTheSecondDirectory_ifMoreThanOneIsPassedIn() {
        String expectedDirectory = "path2";
        String[] args = {"-d", "path1", "-d", "path2"};
        ArgParser parser = new ArgParser();

        ServerOptions options = parser.buildServerOptions(args);
        String directory = options.getDirectory();

        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void buildServerOptions_throwsAnException_ifTheDirectoryFlagIsNotFollowedByAValue() {
        String[] args = {"_", "-d"};
        ArgParser parser = new ArgParser();

        assertThrows(IllegalArgumentException.class, () -> parser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereAreNotAnyValidFlags() {
        String[] args = {"_", "_"};
        ArgParser parser = new ArgParser();

        assertThrows(IllegalArgumentException.class, () -> parser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereIsOnlyOneArgument_andItIsNotAValidFlag() {
        String[] args = {"_"};
        ArgParser parser = new ArgParser();

        assertThrows(IllegalArgumentException.class, () -> parser.buildServerOptions(args));
    }

    @Test
    public void buildServerOptions_throwsAnException_ifThereAreZeroArguments() {
        String[] args = {};
        ArgParser parser = new ArgParser();

        assertThrows(IllegalArgumentException.class, () -> parser.buildServerOptions(args));
    }

}
