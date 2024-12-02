package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day1 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    Pair<List<Integer>, List<Integer>> input = processInput(fileContents);
    Collections.sort(input.getLeft());
    Collections.sort(input.getRight());

    int diffs = 0;
    while (!input.getLeft().isEmpty()) {
      diffs += Math.abs(input.getLeft().remove(0) - input.getRight().remove(0));
    }

    return String.valueOf(diffs);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Pair<List<Integer>, List<Integer>> input = processInput(fileContents);
    return String.valueOf(
        input.getLeft().stream()
            .map(n -> n * times(n, input.getRight()))
            .reduce(0L, Long::sum));
  }

  private Pair<List<Integer>, List<Integer>> processInput(String fileContents) {
    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();

    for (String line : fileContents.split(System.lineSeparator())) {
      int[] parts = Arrays.stream(line.split(" ")).filter(StringUtils::isNotBlank).mapToInt(Integer::parseInt).toArray();
      left.add(parts[0]);
      right.add(parts[1]);
    }

    return Pair.of(left, right);
  }

  private long times(int search, List<Integer> listOfNumbers) {
    return listOfNumbers.stream().filter(n -> n.equals(search)).count();
  }

  public static void main(String[] args) {
    Day.run(Day1::new, "2024/D1_small.txt", "2024/D1_full.txt");
  }
}
