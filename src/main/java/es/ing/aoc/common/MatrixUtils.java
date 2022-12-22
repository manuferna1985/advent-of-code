package es.ing.aoc.common;

import java.lang.reflect.Array;

public class MatrixUtils {

    private MatrixUtils() {
        throw new RuntimeException("Constructor not meant to be called");
    }

    public static void rotateRightAndMove(int[][] matrix, Point offset){
        int[][] result = new int[matrix.length][matrix.length];
        for (int i=0; i< matrix.length; i++){
            for (int j=0; j< matrix.length; j++){
                result[j][matrix.length-i] = matrix[i][j];
            }
        }

        copy(matrix, result, offset);
    }

    public static void copy(int[][] origin, int[][] destination){
        copy(origin, destination, new Point(0, 0));
    }

    public static void copy(int[][] origin, int[][] destination, Point offset){
        for (int i=0; i< origin.length; i++) {
            for (int j = 0; j < origin.length; j++) {
                destination[i+ offset.x][j+ offset.y] = origin[i][j];
            }
        }
    }
}
