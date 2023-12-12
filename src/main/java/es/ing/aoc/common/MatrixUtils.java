package es.ing.aoc.common;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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

  public static <T> T[][] transposeMatrix(Class<T> type, T[][] matrix) {
    T[][] result = (T[][]) Array.newInstance(type, matrix[0].length, matrix.length);
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        result[j][i] = matrix[i][j];
      }
    }
    return result;
  }

  public static <R, T> T[][] transformMatrix(Class<T> type, R[][] matrix, Function<R, T> fn) {
    T[][] result = (T[][]) Array.newInstance(type, matrix.length, matrix[0].length);
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        result[i][j] = fn.apply(matrix[i][j]);
      }
    }
    return result;
  }

  public static <T> T[][] readMatrixFromFile(String fileContents, Class<T> type, Function<String, T> fn) {
    String[] lines = fileContents.split(System.lineSeparator());
    T[][] result = (T[][]) Array.newInstance(type, lines.length, lines[0].length());

    for (int i = 0; i < lines.length; i++) {
      for (int j = 0; j < lines[i].length(); j++) {
        result[i][j] = fn.apply(String.valueOf(lines[i].charAt(j)));
      }
    }
    return result;
  }

  public static <T> void copy(T[][] origin, T[][] destination, Point offset) {
    for (int i = 0; i < origin.length; i++) {
      System.arraycopy(origin[i], 0, destination[i + offset.x], offset.y, origin.length);
    }
  }

  public static <T> void moveSubMatrix(T[][] matrix, Point origin, Point destination, int size, T clearCell) {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        matrix[i + destination.x][j + destination.y] = matrix[i + origin.x][j + origin.y];
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

  public static String[][] readMatrixFromFile(String fileContents) {
    List<String> allLines = Arrays.asList(fileContents.split(System.lineSeparator())); // when input file is multiline
    String[][] matrix = new String[allLines.size()][allLines.get(0).length()];

    String line;
    for (int i = 0; i < allLines.size(); i++) {
      line = allLines.get(i);
      for (int j = 0; j < line.length(); j++) {
        matrix[i][j] = String.valueOf(line.charAt(j));
      }
    }

    return matrix;
  }

  public static List<Pair<Point, String>> getNeighbours(String[][] matrix, int x, int y) {
    return getNeighbours(matrix, x, y, true);
  }

  public static List<Pair<Point, String>> getNeighbours(String[][] matrix, int x, int y, boolean diagonalsAllowed) {
    List<org.apache.commons.lang3.tuple.Pair<Point, String>> neighbours = new ArrayList<>();

    if (x > 0) {
      if (y > 0 && diagonalsAllowed) {
        neighbours.add(buildPointWithValue(matrix, x - 1, y - 1));
      }

      neighbours.add(buildPointWithValue(matrix, x - 1, y));

      if (y < matrix[0].length - 1 && diagonalsAllowed) {
        neighbours.add(buildPointWithValue(matrix, x - 1, y + 1));
      }
    }

    if (x < matrix.length - 1) {
      if (y > 0 && diagonalsAllowed) {
        neighbours.add(buildPointWithValue(matrix, x + 1, y - 1));
      }

      neighbours.add(buildPointWithValue(matrix, x + 1, y));

      if (y < matrix[0].length - 1 && diagonalsAllowed) {
        neighbours.add(buildPointWithValue(matrix, x + 1, y + 1));
      }
    }

    if (y > 0) {
      neighbours.add(buildPointWithValue(matrix, x, y - 1));
    }

    if (y < matrix[0].length - 1) {
      neighbours.add(buildPointWithValue(matrix, x, y + 1));
    }

    return neighbours;
  }

  private static Pair<Point, String> buildPointWithValue(String[][] matrix, int x, int y) {
    return Pair.of(Point.of(x, y), matrix[x][y]);
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
