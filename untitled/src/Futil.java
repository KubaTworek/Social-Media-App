package zad1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Futil {

    public static void processDir(String inputDirectory, String outputFile) {
        Path inputPath = Paths.get(inputDirectory);
        Path outputPath = Paths.get(outputFile);

        try {
            Files.walkFileTree(inputPath, new FileVisitor(outputPath));
        } catch (IOException e) {
            System.err.format("Failed to process directory: %s%n", inputDirectory);
            e.printStackTrace();
        }
    }
}