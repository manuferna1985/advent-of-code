package es.ing.aoc.common;

import java.nio.charset.StandardCharsets;

public abstract class Day {

    protected abstract String part1(String fileContents) throws Exception;

    protected abstract String part2(String fileContents) throws Exception;

    protected Pair<String, String> process(String fileContents) throws Exception {
        return new Pair<>(part1(fileContents), part2(fileContents));
    }

    protected Pair<String, String> go(String filename) {
        Pair<String, String> results = null;
        try (var inp = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            if (inp == null) {
                System.out.println("File not found: " + filename);
                return null;
            }

            String fileContents = new String(inp.readAllBytes(), StandardCharsets.UTF_8);

            System.out.printf("File: %s%n", filename);

            long before = System.nanoTime();
            results = process(fileContents);
            long after = System.nanoTime();

            System.out.println("Part1: " + results.a);
            System.out.println("Part2: " + results.b);

            System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static Pair<String, String> run(DayConstructor factory, String fileName) {
        return factory.build().go(fileName);
    }

    protected static void run(DayConstructor factory, String ... inputs) {
        for (var filename : inputs) {
            factory.build().go(filename);
        }
    }
}
