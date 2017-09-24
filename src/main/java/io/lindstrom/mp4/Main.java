package io.lindstrom.mp4;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private final static Mp4Parser parser = new Mp4Parser();

    private final static Path DEFAULT_INPUT = Paths.get("/Users/anton/code/mp4/src/test/resources/v1-init.mp4");
    //private final static Path DEFAULT_INPUT = Paths.get("src/test/resources/v6-25539.mp4");
    private final static int DEFAULT_ROUNDS = 40;

    public static void main(String[] args) throws Exception {

        final Path input;
        final int rounds;

        if (args.length != 0) {
            input = Paths.get(args[0]);
            rounds = Integer.parseInt(args[1]);
        } else {
            input = DEFAULT_INPUT;
            rounds = DEFAULT_ROUNDS;
        }

        System.out.println("input = " + input);
        System.out.println("rounds = " + rounds);


        mmap(input, rounds);
    }



    private static void mmap(final Path path, final int rounds) throws IOException {
        long total = 0;

        for (int i = 0 ; i < 4; i++) {
            long t = runMmap(path, rounds);
            System.out.println(t);
            total += t;
        }

        System.out.println("mmap: " + total / 4 / 1E9);
    }


    private static long runMmap(final Path input, final int rounds) throws IOException {
        long start = System.nanoTime();
        for (int i = 0; i < rounds; i++) {
            parser.parse(input);
        }
        long end = System.nanoTime();
        return end - start;
    }
}
