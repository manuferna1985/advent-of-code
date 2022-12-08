package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D20_exercise {

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D20_full.txt").toURI()), Charset.defaultCharset());


        // Start coding here. ;-)

        String pattern = allLines.get(0);

        allLines.remove(0);
        allLines.remove(0);

        int initialX = allLines.size();
        int initialY = allLines.get(0).length();
        int[][] matrix = new int[initialX][initialY];

        for (int i = 0; i < initialX; i++) {
            for (int j = 0; j < initialY; j++) {
                matrix[i][j] = allLines.get(i).charAt(j) == '#' ? 1 : 0;
            }
        }

        System.out.println(pattern);
        System.out.println(matrix);
        System.out.println("(initial) --> " + countLights(matrix));

        for (int step = 0; step < 50; step++) {

            matrix = copyAndExpandMatrix(matrix);
            int[][] nextStepMatrix = new int[matrix.length][matrix[0].length];

            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    Integer number = getAdyacentCellsNumber(matrix, i, j, step, pattern.charAt(0) == '#');
                    nextStepMatrix[i][j] = pattern.charAt(number) == '#' ? 1 : 0;
                }
            }
            matrix = nextStepMatrix;

            System.out.println(step + " --> " + countLights(matrix));
        }

        System.out.println(countLights(matrix));
    }

    private static int[][] copyAndExpandMatrix(int[][] matrix){

        int[][] expandedMatrix = new int[matrix.length + 2][matrix[0].length + 2];

        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, expandedMatrix[i + 1], 1, matrix[i].length);
        }

        return expandedMatrix;
    }

    private static Integer getAdyacentCellsNumber(int[][] matrix, int i, int j, int step, boolean firstLight) {

        StringBuilder result = new StringBuilder();

        result.append(getSafeData(matrix, i - 1, j - 1, step, firstLight));
        result.append(getSafeData(matrix, i - 1, j, step, firstLight));
        result.append(getSafeData(matrix, i - 1, j + 1, step, firstLight));

        result.append(getSafeData(matrix, i, j - 1, step, firstLight));
        result.append(getSafeData(matrix, i, j, step, firstLight));
        result.append(getSafeData(matrix, i, j + 1, step, firstLight));

        result.append(getSafeData(matrix, i + 1, j - 1, step, firstLight));
        result.append(getSafeData(matrix, i + 1, j, step, firstLight));
        result.append(getSafeData(matrix, i + 1, j + 1, step, firstLight));

        return Integer.parseInt(result.toString(), 2);
    }

    private static int getSafeData(int[][] matrix, int i, int j, int step, boolean firstLight) {
        if (i >= 1 && i < matrix.length - 1 && j >= 1 && j < matrix[i].length - 1) {
            return matrix[i][j];
        } else {
            return firstLight && (step % 2 == 1) ? 1 : 0;
        }
    }

    private static int countLights(int[][] matrix) {
        int lights = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                lights += matrix[i][j] == 1 ? 1 : 0;
            }
        }
        return lights;
    }
}
