package com.koen.bfe.essentials.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandling {
    public static byte[] readFileContents(String path) throws IOException {
        byte[] data = Files.readAllBytes(Path.of(path));
        return data;
    }
}
