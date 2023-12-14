package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


public class Day14 extends Day {

  private static final PointFunction N = new PointFunction(x -> x - 1, y -> y);
  private static final PointFunction S = new PointFunction(x -> x + 1, y -> y);
  private static final PointFunction E = new PointFunction(x -> x, y -> y + 1);
  private static final PointFunction W = new PointFunction(x -> x, y -> y - 1);

  private static final int ITERATIONS = 1000000000;
  private static final int CACHE_SIZE = 100;
  private static final int LENGTH_FOR_PATTERN_SEARCH = 10;
  private static final char ROCK = 'O';
  private static final char EMPTY = '.';

  record PointFunction(Function<Integer, Integer> xFn, Function<Integer, Integer> yFn) {}

  @Override
  protected String part1(String fileContents) throws Exception {
    Character[][] charsMatrix = MatrixUtils.readMatrixFromFile(fileContents, Character.class, s -> s.charAt(0));
    tiltRocks(charsMatrix, N);
    return String.valueOf(getNorthOverload(charsMatrix));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Character[][] charsMatrix = MatrixUtils.readMatrixFromFile(fileContents, Character.class, s -> s.charAt(0));
    List<Integer> loadSeqs = new ArrayList<>();

    for (int it = 0; it < ITERATIONS; it++) {
      tiltRocks(charsMatrix, N);
      tiltRocks(charsMatrix, W);
      tiltRocks(charsMatrix, S);
      tiltRocks(charsMatrix, E);

      int overload = getNorthOverload(charsMatrix);

      loadSeqs.add(overload);
      if (loadSeqs.size() > CACHE_SIZE) {
        loadSeqs.remove(0);
      }

      int pattern = checkSequenceRepeatPattern(loadSeqs);

      if (pattern > 0) {
        System.out.printf("Pattern detected: %d  %d\n", it, pattern);
        while ((it + pattern) < ITERATIONS) {
          it += pattern;
        }
      }
    }
    return String.valueOf(getNorthOverload(charsMatrix));
  }

  private int checkSequenceRepeatPattern(List<Integer> loadSeqs) {
    if (loadSeqs.size() > LENGTH_FOR_PATTERN_SEARCH) {
      List<Integer> lastN = loadSeqs.subList(loadSeqs.size() - LENGTH_FOR_PATTERN_SEARCH, loadSeqs.size());

      int firstOcurrence = Collections.indexOfSubList(loadSeqs, lastN);
      int lastOcurrence = Collections.lastIndexOfSubList(loadSeqs, lastN);

      if (firstOcurrence > 0 && lastOcurrence > 0) {
        return lastOcurrence - firstOcurrence;
      }
    }
    return 0;
  }

  private int getNorthOverload(Character[][] charsMatrix) {
    int overload = 0;
    for (int x = 0; x < charsMatrix.length; x++) {
      for (int y = 0; y < charsMatrix[x].length; y++) {
        if (ROCK==charsMatrix[x][y]) {
          overload += charsMatrix.length - x;
        }
      }
    }
    return overload;
  }

  private void tiltRocks(Character[][] matrix, PointFunction fn) {
    boolean movs;
    int newX, newY, currX, currY;
    do {
      movs = false;
      for (int x = 0; x < matrix.length; x++) {
        for (int y = 0; y < matrix[x].length; y++) {
          currX = x;
          currY = y;
          if (ROCK==matrix[x][y]) {
            boolean currentRockRoll;
            do {
              currentRockRoll = false;
              newX = fn.xFn().apply(currX);
              newY = fn.yFn().apply(currY);

              if (pointIsWithinLimits(matrix, newX, newY) && EMPTY==matrix[newX][newY]) {
                movs = true;
                currentRockRoll = true;
                matrix[currX][currY] = EMPTY;
                matrix[newX][newY] = ROCK;

                currX = newX;
                currY = newY;
              }
            } while (currentRockRoll);
          }
        }
      }
    } while (movs);
  }

  private boolean pointIsWithinLimits(Character[][] matrix, int newX, int newY) {
    return newX >= 0 && newX < matrix.length && newY >= 0 && newY < matrix[0].length;
  }

  public static void main(String[] args) {
    Day.run(Day14::new, "2023/D14_small.txt", "2023/D14_full.txt");
  }
}
