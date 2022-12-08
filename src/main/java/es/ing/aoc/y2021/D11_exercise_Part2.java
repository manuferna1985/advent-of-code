package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D11_exercise_Part2 {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D11_exercise_Part2.class.getResource("2021/D11_full.txt").toURI()), Charset.defaultCharset());


        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
            }
        }


        // Steps

        Long flashCounter = 0L;

        List<Integer> currentFlashes = new ArrayList<>();

        for (int step=1; step<=10000; step++) {

            currentFlashes.clear();

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j]++;
                }
            }

            boolean newFlashes;
            do{
                newFlashes = false;

                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        Integer id = i * 1000 + j;
                        if (matrix[i][j] > 9 && !currentFlashes.contains(id)) {
                            flash(matrix, i, j);
                            currentFlashes.add(id);
                            flashCounter++;
                            newFlashes = true;
                        }
                    }
                }
            } while(newFlashes);

            if (currentFlashes.size() == 100){
                System.out.println("Step: " + step);
                break;
            }

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] > 9) {
                        matrix[i][j] = 0;
                    }
                }
            }
        }

        System.out.println("Flashes: " + flashCounter);
    }

    private static void flash(int[][] matrix, int i, int j) {

        // N
        if (i > 0) {
            matrix[i - 1][j]++;
        }

        // S
        if (i < matrix.length - 1) {
            matrix[i + 1][j]++;
        }

        // E
        if (j < matrix.length - 1) {
            matrix[i][j + 1]++;
        }

        // W
        if (j > 0) {
            matrix[i][j - 1]++;
        }

        // NE
        if (i > 0 &&  j < matrix.length - 1) {
            matrix[i - 1][j + 1]++;
        }

        // NW
        if (i > 0 && j > 0) {
            matrix[i - 1][j - 1]++;
        }

        // SE
        if (i < matrix.length - 1 && j < matrix.length - 1) {
            matrix[i + 1][j + 1]++;
        }

        // SW
        if (i < matrix.length - 1 && j > 0) {
            matrix[i + 1][j - 1]++;
        }
    }
}
