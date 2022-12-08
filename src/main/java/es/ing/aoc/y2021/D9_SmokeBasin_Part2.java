package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D9_SmokeBasin_Part2 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part2.class.getResource("2021/D9_full.txt").toURI()), Charset.defaultCharset());

        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        // Find lowest points
        List<Integer> bases = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Long riskLevel = findRiskLevelFor(i, j, matrix);

                if (riskLevel != 0L) {
                    List<Integer> basePoints = new ArrayList<>();
                    countBaseSize(basePoints, i, j, matrix);
                    System.out.printf("Base [%d,%d]: %d%n", i, j, basePoints.size());
                    bases.add(basePoints.size());
                }
            }
        }

        Collections.sort(bases, Collections.reverseOrder());

        System.out.println(matrix);
        System.out.println("Together: " + bases.get(0) * bases.get(1) * bases.get(2));
    }

    private static void countBaseSize(List<Integer> basePoints, int i, int j, int[][] matrix) {

        int currentValue = i * 1000 + j;
        if (!basePoints.contains(currentValue)) {
            basePoints.add(currentValue);


            // Up
            if (i > 0 && sameBase(matrix[i][j], matrix[i - 1][j])) {
                countBaseSize(basePoints, i - 1, j, matrix);
            }

            // Down
            if (i < matrix.length - 1 && sameBase(matrix[i][j], matrix[i + 1][j])) {
                countBaseSize(basePoints, i + 1, j, matrix);
            }

            // Left
            if (j > 0 && sameBase(matrix[i][j], matrix[i][j - 1])) {
                countBaseSize(basePoints, i, j - 1, matrix);
            }

            // Right
            if (j < matrix[i].length - 1 && sameBase(matrix[i][j], matrix[i][j + 1])) {
                countBaseSize(basePoints, i, j + 1, matrix);
            }

            //System.out.printf(">>> [%d,%d]%n", i, j);
        }
    }

    private static boolean sameBase(int center, int candidate) {
        return candidate < 9 && candidate > center;
    }

    private static Long findRiskLevelFor(int i, int j, int[][] matrix) {

        List<Integer> adjacentPoints = new ArrayList<>();

        if (i > 0) {
            adjacentPoints.add(matrix[i - 1][j]);
        }

        if (i < matrix.length - 1) {
            adjacentPoints.add(matrix[i + 1][j]);
        }

        if (j > 0) {
            adjacentPoints.add(matrix[i][j - 1]);
        }

        if (j < matrix[i].length - 1) {
            adjacentPoints.add(matrix[i][j + 1]);
        }

        Collections.sort(adjacentPoints);

        if (matrix[i][j] < adjacentPoints.get(0)) {
            return (long) (matrix[i][j] + 1);
        } else {
            return 0L;
        }
    }
}
