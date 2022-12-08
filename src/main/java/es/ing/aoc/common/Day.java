package es.ing.aoc.common;

import java.nio.charset.StandardCharsets;

public abstract class Day {

    protected abstract void part1(String fileContents) throws Exception;

    protected abstract void part2(String fileContents) throws Exception;

    protected void process(String fileContents) throws Exception {
        part1(fileContents);
        part2(fileContents);
    }

    protected void go(String filename) {
        try (var inp = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            if (inp == null) {
                System.out.println("File not found: " + filename);
                return;
            }

            String fileContents = new String(inp.readAllBytes(), StandardCharsets.UTF_8);

            System.out.printf("File: %s%n", filename);

            long before = System.nanoTime();
            process(fileContents);
            long after = System.nanoTime();

            System.out.printf("Elapsed time: %.0f ms%n%n", ((double)(after-before))/1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void run(DayConstructor factory, String ... inputs) {
        for (var filename : inputs) {
            factory.build().go(filename);
        }
    }
}
