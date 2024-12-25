package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day25 extends Day {

  private static final Integer MAX_OVERLAP = 5;

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] blocks = fileContents.split(System.lineSeparator() + System.lineSeparator());

    List<List<Byte>> keys = new ArrayList<>();
    List<List<Byte>> locks = new ArrayList<>();

    for (String b : blocks) {
      List<Byte> combination = getMatrixLockCombination(b);
      if (b.substring(0, b.indexOf(System.lineSeparator())).contains("#")) {
        // Locks
        locks.add(combination);
      } else {
        // Keys
        keys.add(combination);
      }
    }

    int matchingKeys = 0;
    for (var key : keys) {
      for (var lock : locks) {
        matchingKeys += keyMatchesLock(key, lock) ? 1:0;
      }
    }

    return String.valueOf(matchingKeys);
  }

  private boolean keyMatchesLock(List<Byte> key, List<Byte> lock) {
    return IntStream.range(0, key.size())
        .allMatch(i -> key.get(i) + lock.get(i) <= MAX_OVERLAP);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  private List<Byte> getMatrixLockCombination(String block) {
    Boolean[][] matrix = MatrixUtils.readMatrixFromFile(block, Boolean.class, ch -> ch.equals("#"));
    List<Byte> locks = new ArrayList<>();

    if (matrix[0][0]) {
      // Locks
      for (int j = 0; j < matrix[0].length; j++) {
        byte n = -1;
        for (int i = 0; i < matrix.length; i++) {
          if (matrix[i][j]) {
            n++;
          } else {
            break;
          }
        }
        locks.add(n);
      }
    } else {
      // Keys
      for (int j = 0; j < matrix[0].length; j++) {
        byte n = -1;
        for (int i = matrix.length - 1; i >= 0; i--) {
          if (matrix[i][j]) {
            n++;
          } else {
            break;
          }
        }
        locks.add(n);
      }
    }

    return locks;
  }

  public static void main(String[] args) {
    Day.run(Day25::new, "2024/D25_small.txt", "2024/D25_full.txt");
  }
}
