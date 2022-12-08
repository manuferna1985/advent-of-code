package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D7_Whale_Part2 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part2.class.getResource("2021/D7_full.txt").toURI()), Charset.defaultCharset());

        List<Integer> crabs = Arrays.stream(allLines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());

        Integer currentCrab;
        int minFuel = Integer.MAX_VALUE;
        Integer minCrabPosition = null;

        for (int i = 0; i < crabs.stream().max(Integer::compare).get(); i++) {
            currentCrab = i;

            Integer finalCurrentCrab = currentCrab;
            int currentFuel = crabs.stream().mapToInt(Integer::intValue).map(pos -> Math.abs(calculateFuel(finalCurrentCrab, pos))).sum();

            System.out.println(String.format("%d -> %d", currentCrab, currentFuel));

            if (currentFuel < minFuel) {
                minFuel = currentFuel;
                minCrabPosition = currentCrab;
            }
        }

        System.out.println("Crab position: " + minCrabPosition);
        System.out.println("Min fuel: " + minFuel);
    }

    private static int calculateFuel(int pos1, int pos2) {
        return calculateFuel(pos1, pos2, 1);
    }

    private static int calculateFuel(int pos1, int pos2, int multiplier) {
        if (pos1 == pos2) {
            return 0;
        }
        return multiplier + calculateFuel(Math.min(pos1, pos2) + 1, Math.max(pos1, pos2), multiplier + 1);
    }


}
