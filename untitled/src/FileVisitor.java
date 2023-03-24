package zad1;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileVisitor extends SimpleFileVisitor<Path> {

    private static final int BUFFER_SIZE = 8192;

    private final Charset inCharset = Charset.forName("Cp1250");
    private final Charset outCharset = Charset.forName("UTF-8");

    private final Path outputFile;

    public FileVisitor(Path outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (!attrs.isRegularFile()) {
            return CONTINUE;
        }

        System.out.format("Processing file: %s (%d bytes)%n", file, attrs.size());

        try (FileChannel input = FileChannel.open(file);
             FileChannel output = FileChannel.open(outputFile, EnumSet.of(CREATE, APPEND))) {

            ByteBuffer inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            ByteBuffer outputBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (input.read(inputBuffer) != -1) {
                inputBuffer.flip();
                CharBuffer charBuffer = inCharset.decode(inputBuffer);
                outputBuffer.clear();
                outputBuffer.put(outCharset.encode(charBuffer));
                outputBuffer.flip();
                output.write(outputBuffer);
                inputBuffer.compact();
            }

        } catch (IOException e) {
            System.err.format("Failed to process file: %s%n", file);
            e.printStackTrace();
        }

        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException e) {
        System.err.format("Failed to visit file: %s%n", file);
        e.printStackTrace();
        return CONTINUE;
    }
}