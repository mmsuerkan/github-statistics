package com.example.githubcase.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileWriter {

    public static boolean writeToFile(String fileName, List<String> content, String directory) {
        Path filePath = Path.of(directory, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            System.out.println("Successfully written to file" + filePath);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to file" + filePath);
        }
        return true;
    }
}
