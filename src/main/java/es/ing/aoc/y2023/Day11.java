package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MathUtils;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.RangeUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 extends Day {

  private static final String GALAXY = "#";
  private static final String SPACE = ".";
  private static final Integer MULTIPLIER_PART1 = 2;
  private static final Integer MULTIPLIER_PART2 = 1000000;

  @Override
  protected String part1(String fileContents) throws Exception {
    int distance = getShortestPathBetweenGalaxies(
        getGalaxyPairs(getGalaxiesCoordinates(expandGalaxy(MatrixUtils.readMatrixFromFile(fileContents), MULTIPLIER_PART1))));
    return String.valueOf(distance);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    int distance = getShortestPathBetweenGalaxies(
        getGalaxyPairs(getGalaxiesCoordinates(expandGalaxy(MatrixUtils.readMatrixFromFile(fileContents), MULTIPLIER_PART1))));

    int distanceNext = getShortestPathBetweenGalaxies(
        getGalaxyPairs(getGalaxiesCoordinates(expandGalaxy(MatrixUtils.readMatrixFromFile(fileContents), MULTIPLIER_PART1 + 1))));

    BigInteger inc = BigInteger.valueOf(distanceNext - distance);
    BigInteger multiplier = BigInteger.valueOf(MULTIPLIER_PART2);
    multiplier = multiplier.subtract(BigInteger.valueOf(MULTIPLIER_PART1 + 1));

    return String.valueOf(inc.multiply(multiplier).add(BigInteger.valueOf(distanceNext)));
  }

  private String[][] expandGalaxy(String[][] matrix, int factor) {
    return MatrixUtils.transposeMatrix(
        String.class,
        expandEmptyRows(
            MatrixUtils.transposeMatrix(
                String.class,
                expandEmptyRows(matrix, factor)),
            factor));
  }

  private String[][] expandEmptyRows(String[][] matrix, int factor) {
    List<String[]> rows = new ArrayList<>();
    for (String[] strings : matrix) {
      if (Arrays.stream(strings).allMatch(SPACE::equals)) {
        for (int x = 0; x < factor; x++) {
          rows.add(strings);
        }
      } else {
        rows.add(strings);
      }
    }
    return rows.toArray(new String[0][0]);
  }

  private List<Point> getGalaxiesCoordinates(String[][] matrix) {
    List<Point> galaxies = new ArrayList<>();
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        if (GALAXY.equals(matrix[x][y])) {
          galaxies.add(Point.of(x, y));
        }
      }
    }
    return galaxies;
  }

  private List<Pair<Point, Point>> getGalaxyPairs(List<Point> galaxies) {
    return MathUtils.generateCombinationsFor(galaxies, 2).stream().map(comb -> Pair.of(comb.get(0), comb.get(1))).toList();
  }

  private Integer getShortestPathBetweenGalaxies(List<Pair<Point, Point>> galaxyDistances) {
    return galaxyDistances.stream().map(dist -> RangeUtils.getManhattanDistance(dist.getLeft(), dist.getRight())).mapToInt(Integer::intValue).sum();
  }

  public static void main(String[] args) {
    Day.run(Day11::new, "2023/D11_small.txt", "2023/D11_full.txt");
  }
}
