package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 extends Day {

  private static final char UNKNOWN = '?';
  private static final char OPERATIONAL = '.';
  private static final char DAMAGED = '#';

  private final Map<Pair<String, List<Integer>>, Long> cache = new HashMap<>();

  @Override
  protected String part1(String fileContents) throws Exception {
    return algorithm(fileContents, 1);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return algorithm(fileContents, 5);
  }

  private String algorithm(String fileContents, int folding) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<Pair<String, List<Integer>>> info = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(StringUtils.SPACE);
      info.add(fillRecords(
          Pair.of(parts[0], Arrays.stream(parts[1].split(",")).mapToInt(Integer::parseInt).boxed().toList()), folding));
    }

    return String.valueOf(info.stream().map(rec -> calculateCombinationsInit(rec.getLeft(), rec.getRight())).mapToLong(Long::longValue).sum());
  }

  private Pair<String, List<Integer>> fillRecords(Pair<String, List<Integer>> info, int n) {
    StringBuilder newRecords = new StringBuilder();
    List<Integer> newNumbers = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      newRecords.append(info.getLeft());
      newNumbers.addAll(info.getRight());
      if (i < n - 1) {
        newRecords.append(UNKNOWN);
      }
    }
    return Pair.of(newRecords.toString(), newNumbers);
  }

  private long calculateCombinationsInit(String str, List<Integer> conditionRecords) {
    cache.clear();
    return calculateCombinationsWithCache(str, conditionRecords);
  }

  private long calculateCombinationsWithCache(String str, List<Integer> conditionRecords) {
    var key = Pair.of(str, conditionRecords);
    if (cache.containsKey(key)) {
      return cache.get(key);
    } else {
      long result = calculateCombinations(str, conditionRecords);
      cache.put(key, result);
      return result;
    }
  }

  private long calculateCombinations(String str, List<Integer> conditionRecords) {
    if (str.isEmpty()) {
      return Boolean.compare(conditionRecords.isEmpty(), false);
    } else {
      return switch (str.charAt(0)) {
        case UNKNOWN -> processUnknownSpring(str, conditionRecords);
        case OPERATIONAL -> calculateCombinationsWithCache(str.substring(1), conditionRecords);
        case DAMAGED -> processDamagedSpring(str, conditionRecords);
        default -> throw new IllegalStateException("Unexpected value: " + str.charAt(0));
      };
    }
  }

  private long processUnknownSpring(String str, List<Integer> conditionRecords) {
    return calculateCombinations(OPERATIONAL + str.substring(1), conditionRecords)
        + calculateCombinations(DAMAGED + str.substring(1), conditionRecords);
  }

  private long processDamagedSpring(String str, List<Integer> conditionRecords) {
    if (conditionRecords.isEmpty()) {
      return 0L;
    }

    int currentGroupLength = conditionRecords.get(0);
    if (isSpringSeqTooShort(str, currentGroupLength)) {
      return 0L;
    }
    if (conditionRecords.size() > 1) {
      if (!isSequenceCorrectlySeparated(str, currentGroupLength)) {
        return 0L;
      }
      // Next sequence
      return calculateCombinations(
          str.substring(currentGroupLength + 1),
          getNextRecords(conditionRecords));
    }

    // Last sequence
    return calculateCombinations(
        str.substring(currentGroupLength),
        getNextRecords(conditionRecords));
  }

  private static List<Integer> getNextRecords(List<Integer> conditionRecords) {
    return conditionRecords.stream().skip(1).toList();
  }

  private boolean isSpringSeqTooShort(String str, int expectedGroupLength) {
    return str.length() < expectedGroupLength || str.substring(0, expectedGroupLength).contains(String.valueOf(OPERATIONAL));
  }

  private boolean isSequenceCorrectlySeparated(String str, int length) {
    return str.length() > length + 1 && DAMAGED!=str.charAt(length);
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2023/D12_small.txt", "2023/D12_full.txt");
  }
}
