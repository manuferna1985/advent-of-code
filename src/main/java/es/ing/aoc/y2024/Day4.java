package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Day4 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    List<String> matrixLines = new ArrayList<>();

    for (int i = 0; i < matrix.length; i++) {
      StringBuilder lineH = new StringBuilder();
      StringBuilder lineV = new StringBuilder();
      for (int j = 0; j < matrix[i].length; j++) {
        lineH.append(matrix[i][j]);
        lineV.append(matrix[j][i]);
      }
      matrixLines.add(lineH.toString());
      matrixLines.add(lineV.toString());
    }

    addDiagonals(matrix, matrixLines);
    MatrixUtils.rotateLeft(String.class, matrix, Point.of(0, 0), matrix.length);
    addDiagonals(matrix, matrixLines);

    return String.valueOf(
        matrixLines.stream()
            .map(line -> StringUtils.countMatches(line, "XMAS") + StringUtils.countMatches(line, "SAMX"))
            .mapToInt(Integer::intValue)
            .sum());
  }

  private void addDiagonals(String[][] matrix, List<String> matrixLines) {
    for (int i = 0; i < matrix.length; i++) {
      matrixLines.add(getLineDiagonal(matrix, 0, i));
      if (i > 0) {
        matrixLines.add(getLineDiagonal(matrix, i, 0));
      }
    }
  }

  private String getLineDiagonal(String[][] matrix, int xStart, int yStart) {
    StringBuilder line = new StringBuilder();
    for (int i = xStart, j = yStart; i < matrix.length && j < matrix.length; i++, j++) {
      line.append(matrix[i][j]);
    }
    return line.toString();
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    int count = 0;
    for (int i = 1; i < matrix.length - 1; i++) {
      for (int j = 1; j < matrix[i].length - 1; j++) {
        if ("A".equals(matrix[i][j]) && getWords(matrix, i, j).stream().allMatch(w -> "SAM".equals(w) || "MAS".equals(w))) {
          count++;
        }
      }
    }
    return String.valueOf(count);
  }

  private List<String> getWords(String[][] matrix, int i, int j) {
    return List.of(
        matrix[i - 1][j - 1] + matrix[i][j] + matrix[i + 1][j + 1],
        matrix[i + 1][j - 1] + matrix[i][j] + matrix[i - 1][j + 1]);
  }

  public static void main(String[] args) {
    Day.run(Day4::new, "2024/D4_small.txt", "2024/D4_full.txt");
  }
}
