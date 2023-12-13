package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day13 extends Day {

  private static final String SEP = System.lineSeparator();

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(getResult(fileContents, true));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(getResult(fileContents, false));
  }

  private int getResult(String fileContents, boolean exactMatch) {
    return Arrays.stream(fileContents.split(SEP + SEP))
        .map(group -> Arrays.stream(group.split(SEP))
            .map(line -> line.split(StringUtils.EMPTY))
            .toArray(String[][]::new))
        .map(matrix -> calculateReflection(matrix, exactMatch))
        .mapToInt(Integer::valueOf)
        .sum();
  }

  private int calculateReflection(String[][] matrix, boolean exactMatch) {
    return getHorizontalReflection(matrix, exactMatch) * 100 + getVerticalReflection(matrix, exactMatch);
  }

  private int getVerticalReflection(String[][] matrix, boolean exactMatch) {
    return getHorizontalReflection(MatrixUtils.transposeMatrix(String.class, matrix), exactMatch);
  }

  private int getHorizontalReflection(String[][] matrix, boolean exactMatch) {
    int remainingSmudges, diffs;

    for (int x = 0; x < matrix.length - 1; x++) {
      remainingSmudges = exactMatch ? 0:1;
      diffs = getDifferencesBetween(matrix[x], matrix[x + 1]);

      if (diffs==0 || (diffs==1 && remainingSmudges-- > 0)) {
        boolean match = true;
        for (int xA = x - 1, xB = x + 2; xA >= 0 && xB < matrix.length; xA--, xB++) {

          diffs = getDifferencesBetween(matrix[xA], matrix[xB]);

          if (!(diffs==0 || (diffs==1 && remainingSmudges-- > 0))) {
            match = false;
            break;
          }
        }
        if (match && (exactMatch || remainingSmudges==0)) {
          return x + 1;
        }
      }
    }
    return 0;
  }

  private int getDifferencesBetween(String[] row1, String[] row2) {
    return IntStream.rangeClosed(0, row1.length - 1).map(i -> row1[i].equals(row2[i]) ? 0:1).sum();
  }

  public static void main(String[] args) {
    Day.run(Day13::new, "2023/D13_small.txt", "2023/D13_full.txt");
  }
}
