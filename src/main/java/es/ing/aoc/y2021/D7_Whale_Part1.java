package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D7_Whale_Part1 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part2.class.getResource("2021/D7_full.txt").toURI()), Charset.defaultCharset());

        List<Integer> crabs = Arrays.stream(allLines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        Integer currentCrab;
        int minFuel = Integer.MAX_VALUE;
        Integer minCrabPosition = null;

        for (int i = 0; i < crabs.size(); i++) {
            currentCrab = crabs.get(i);

            Integer finalCurrentCrab = currentCrab;
            int currentFuel = crabs.stream().mapToInt(Integer::intValue).map(pos -> Math.abs(finalCurrentCrab - pos)).sum();

            if (currentFuel < minFuel) {
                minFuel = currentFuel;
                minCrabPosition = currentCrab;
            }
        }

        System.out.println("Crab position: " + minCrabPosition);
        System.out.println("Min fuel: " + minFuel);
    }
}
