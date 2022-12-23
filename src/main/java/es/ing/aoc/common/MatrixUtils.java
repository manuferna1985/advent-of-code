package es.ing.aoc.common;

import java.lang.reflect.Array;

public class MatrixUtils {

    private MatrixUtils() {
        throw new RuntimeException("Constructor not meant to be called");
    }

    @SuppressWarnings("unchecked")
    public static <T> void rotateRight(Class<T> type, T[][] matrix, Point offset, int size) {
        T[][] result = (T[][]) Array.newInstance(type, size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[j][size - i - 1] = matrix[i + offset.x][j + offset.y];
            }
        }
        copy(result, matrix, offset);
    }

    @SuppressWarnings("unchecked")
    public static <T> void rotateLeft(Class<T> type, T[][] matrix, Point offset, int size) {
        T[][] result = (T[][]) Array.newInstance(type, size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = matrix[j + offset.x][size - i - 1 + offset.y];
            }
        }
        copy(result, matrix, offset);
    }

    public static <T> void copy(T[][] origin, T[][] destination, Point offset) {
        for (int i = 0; i < origin.length; i++) {
            System.arraycopy(origin[i], 0, destination[i + offset.x], offset.y, origin.length);
        }
    }

    public static <T> void moveSubMatrix(T[][] matrix, Point origin, Point destination, int size, T clearCell){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i+ destination.x][j + destination.y] = matrix[i + origin.x][j + origin.y];
                matrix[i + origin.x][j + origin.y] = clearCell;
            }
        }
    }

    public static <T> void printMatrix(T[][] matrix) {
        System.out.println("-------------------------------");
        for (T[] row : matrix) {
            for (T cell : row) {
                System.out.printf("%-3s", cell);
            }
            System.out.println();
        }
        System.out.println("-------------------------------");
    }

    public static void main(String[] args) {

        Integer[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}};

        printMatrix(matrix);
        rotateRight(Integer.class, matrix, new Point(0, 0), 2);
        printMatrix(matrix);
        moveSubMatrix(matrix, new Point(0, 0), new Point(2, 2), 2, 0);
        printMatrix(matrix);
    }
}
