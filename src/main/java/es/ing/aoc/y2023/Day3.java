package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class Day3 extends Day {

  private static final String EMPTY = ".";

  @Override
  protected String part1(String fileContents) throws Exception {

    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);

    Long sum = 0L;

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {

        if (NumberUtils.isDigits(matrix[x][y])) {
          // Number detected
          List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(matrix, x, y);

          if (neighbours.stream().map(Pair::getRight).anyMatch(this::isSymbol)) {
            sum += getPartNumberFromPosition(matrix, x, y, true).getValue();
          }
        }
      }
    }

    return String.valueOf(sum);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);

    Long sum = 0L;

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {

        if (this.isSymbol(matrix[x][y])) {
          // Symbol detected
          List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(matrix, x, y);

          List<Pair<Point, Long>> partNumbers = neighbours.stream()
              .filter(n -> NumberUtils.isDigits(n.getValue()))
              .map(n -> this.getPartNumberFromPosition(matrix, n.getLeft().x, n.getLeft().y, false))
              .distinct()
              .collect(Collectors.toList());

          if (partNumbers.size() > 1) {
            sum += partNumbers.stream().map(Pair::getValue).reduce(1L, (p1, p2) -> p1 * p2);
          }
        }
      }
    }


    return String.valueOf(sum);
  }

  private Pair<Point, Long> getPartNumberFromPosition(String[][] matrix, int x, int y, boolean cleanMatrix) {

    int yMin = y, yMax = y;
    String result = matrix[x][y];
    if (cleanMatrix) {
      matrix[x][y] = EMPTY;
    }

    while (yMin > 0 && NumberUtils.isDigits(matrix[x][yMin - 1])) {
      yMin--;
      result = matrix[x][yMin] + result;
      if (cleanMatrix) {
        matrix[x][yMin] = EMPTY;
      }
    }

    while (yMax < matrix[x].length - 1 && NumberUtils.isDigits(matrix[x][yMax + 1])) {
      yMax++;
      result = result + matrix[x][yMax];
      if (cleanMatrix) {
        matrix[x][yMax] = EMPTY;
      }
    }

    return Pair.of(Point.of(x, yMin), Long.parseLong(result));
  }

  private boolean isSymbol(String input) {
    return !NumberUtils.isDigits(input) && !input.equals(".");
  }

  public static void main(String[] args) {
    Day.run(Day3::new, "2023/D3_small.txt", "2023/D3_full.txt");
  }
}
