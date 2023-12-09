package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;

public class Day9 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(
        Arrays.stream(fileContents.split(System.lineSeparator()))
            .map(line -> Arrays.stream(line.split(StringUtils.SPACE))
                .mapToLong(Long::parseLong).boxed().toList())
            .map(this::getNextNumberFrom).mapToLong(Long::longValue)
            .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(
        Arrays.stream(fileContents.split(System.lineSeparator()))
            .map(line -> Arrays.stream(line.split(StringUtils.SPACE))
                .mapToLong(Long::parseLong).boxed().toList())
            .map(this::getPreviousNumberFrom).mapToLong(Long::longValue)
            .sum());
  }

  private Long getNextNumberFrom(List<Long> sequence) {
    return ListUtils.last(executeSeriesLoop(sequence,
        (last, previous) -> previous.add(previous.get(previous.size() - 1) + last.get(last.size() - 1))));
  }

  private Long getPreviousNumberFrom(List<Long> sequence) {
    return ListUtils.first(executeSeriesLoop(sequence,
        (last, previous) -> previous.add(0, previous.get(0) + (-1 * last.get(0)))));
  }

  private List<Long> executeSeriesLoop(List<Long> sequence, BiConsumer<List<Long>, List<Long>> fn) {
    Deque<List<Long>> stack = getSeriesStacked(sequence);
    stack.peek().add(0L);

    while (stack.size() > 1) {
      List<Long> last = stack.pop();
      List<Long> previous = stack.peek();
      fn.accept(last, previous);
    }

    return stack.pop();
  }

  private Deque<List<Long>> getSeriesStacked(List<Long> sequence) {
    boolean allZeros = false;

    Deque<List<Long>> stack = new ArrayDeque<>();
    stack.add(new ArrayList<>(sequence));

    while (!allZeros) {
      List<Long> current = stack.peek();
      List<Long> diffs = new ArrayList<>();
      for (int i = 0; i < current.size() - 1; i++) {
        diffs.add(current.get(i + 1) - current.get(i));
      }
      stack.push(diffs);

      if (diffs.stream().allMatch(a -> a==0)) {
        allZeros = true;
      }
    }
    return stack;
  }

  public static void main(String[] args) {
    Day.run(Day9::new, "2023/D9_small.txt", "2023/D9_full.txt");
  }
}
