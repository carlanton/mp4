package io.lindstrom.mp4;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private final static Mp4Parser parser = new Mp4Parser();
    private final static Path input = Paths.get("/Users/anton/code/mp4/src/test/resources/v1-init.mp4");

    public static void main(String[] args) throws Exception {
        long total = 0;

        System.out.println("warmup");
        for (int i = 0; i < 10; i++) {
            System.out.println(run()); // warmup
        }

        System.out.println("real");
        for (int i = 0 ; i < 4; i++) {
            long t = run();
            System.out.println(t);
            total += t;
        }

        System.out.println("avg: " + total / 4 / 1E9);
    }

    private static long run() throws IOException {
        long start = System.nanoTime();
        for (int i = 0; i < 40_000; i++) {
            try (ReadableByteChannel channel = Files.newByteChannel(input)) {
                parser.parse(channel);
            }
        }
        long end = System.nanoTime();
        return end - start;
    }
}
