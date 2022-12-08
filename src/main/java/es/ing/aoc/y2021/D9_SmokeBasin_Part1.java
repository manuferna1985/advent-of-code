package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D9_SmokeBasin_Part1 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D9_SmokeBasin_Part1.class.getResource("2021/D9_full.txt").toURI()), Charset.defaultCharset());

        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++){
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }

        // Find lowest points

        Long riskLevel = 0L;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                riskLevel += findRiskLevelFor(i, j, matrix);
            }
        }


        System.out.println(matrix);
        System.out.println("RiskLevel: " + riskLevel);
    }

    private static Long findRiskLevelFor(int i, int j, int[][] matrix) {

        List<Integer> adjacentPoints = new ArrayList<>();

        if (i > 0) {
            adjacentPoints.add(matrix[i-1][j]);
        }

        if (i < matrix.length - 1) {
            adjacentPoints.add(matrix[i+1][j]);
        }

        if (j > 0) {
            adjacentPoints.add(matrix[i][j-1]);
        }

        if (j < matrix[i].length - 1) {
            adjacentPoints.add(matrix[i][j+1]);
        }

        Collections.sort(adjacentPoints);

        if (matrix[i][j] < adjacentPoints.get(0)){
            return Long.valueOf(matrix[i][j] + 1);
        } else {
            return 0L;
        }
    }
}
