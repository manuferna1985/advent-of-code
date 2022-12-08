package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class D25_exercise {

    private static final byte EMPTY = 0;
    private static final byte RIGHT = 1;
    private static final byte DOWN = 2;

    public static void main(String[] args) throws Exception {
        execute("2021/D25_small.txt");
        execute("2021/D25_full.txt");
    }

    public static void execute(String fileName) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D25_exercise.class.getResource(fileName).toURI()), Charset.defaultCharset());

        // Start coding here. ;-)
        long before = System.nanoTime();

        int initialX = allLines.size();
        int initialY = allLines.get(0).length();
        byte[][] matrix = new byte[initialX][initialY];

        for (int i = 0; i < initialX; i++) {
            for (int j = 0; j < initialY; j++) {
                switch (allLines.get(i).charAt(j)) {
                    case '>':
                        matrix[i][j] = RIGHT;
                        break;
                    case 'v':
                        matrix[i][j] = DOWN;
                        break;
                    default:
                        matrix[i][j] = EMPTY;
                        break;
                }
            }
        }

        printMatrix(matrix);

        int moved, n = 0;
        do {
            moved = moveFishes(matrix);
            n++;
        } while (moved > 0);

        printMatrix(matrix);
        System.out.println("Iterations: " + n);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
    }

    private static int moveFishes(byte[][] matrix) {
        int moved = 0;
        byte[][] copyMatrix;

        copyMatrix = copyMatrix(matrix);
        // Right movement
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == RIGHT) {
                    // >
                    int rightColumn = (j + 1) % matrix[i].length;
                    if (copyMatrix[i][rightColumn] == EMPTY) {
                        matrix[i][j] = EMPTY;
                        matrix[i][rightColumn] = RIGHT;
                        moved++;
                        j++;
                    }
                }
            }
        }

        copyMatrix = copyMatrix(matrix);
        // Down  movement
        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] == DOWN) {
                    // v
                    int bottomRow = (i + 1) % matrix.length;
                    if (copyMatrix[bottomRow][j] == EMPTY) {
                        matrix[i][j] = EMPTY;
                        matrix[bottomRow][j] = DOWN;
                        moved++;
                        i++;
                    }
                }
            }
        }

        return moved;
    }

    private static byte[][] copyMatrix(byte[][] original) {
        byte[][] newMatrix = new byte[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                newMatrix[i][j] = original[i][j];
            }
        }
        return newMatrix;
    }

    private static void printMatrix(byte[][] matrix) {
        System.out.println("-------------------------------------------");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                switch (matrix[i][j]) {
                    case 1:
                        System.out.print(">");
                        break;
                    case 2:
                        System.out.print("v");
                        break;
                    default:
                        System.out.print(".");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
    }
}
