package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day19 extends Day {

  private final Map<String, Long> cache = new HashMap<>();

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(getTowelCombinations(fileContents).filter(r -> r > 0).count());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(getTowelCombinations(fileContents).mapToLong(Long::valueOf).sum());
  }

  private Stream<Long> getTowelCombinations(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    List<String> towels = Arrays.stream(lines[0].split(", ")).toList();
    List<String> designsToCheck = IntStream.rangeClosed(2, lines.length - 1).mapToObj(i -> lines[i]).toList();
    return designsToCheck.stream().map(design -> checkDesignCached(design, towels));
  }

  private long checkDesignCached(String design, List<String> towels) {
    if (cache.containsKey(design)) {
      return cache.get(design);
    }
    long result = checkDesign(design, towels);
    cache.put(design, result);
    return result;
  }

  private long checkDesign(String design, List<String> towels) {
    if (design.isEmpty()) {
      return 1L;
    }
    return towels.stream()
        .filter(design::startsWith)
        .map(t -> checkDesignCached(design.substring(t.length()), towels))
        .mapToLong(Long::longValue)
        .sum();
  }

  public static void main(String[] args) {
    Day.run(Day19::new, "2024/D19_small.txt", "2024/D19_full.txt");
  }
}
