package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(blinkAlgorithm(fileContents, 25));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(blinkAlgorithm(fileContents, 75));
  }

  private Long blinkAlgorithm(String fileContents, int timesToBlink) {
    List<Long> originalRocks = Arrays.stream(fileContents.split(System.lineSeparator())[0].split(" ")).map(Long::parseLong).toList();

    Map<Long, Long> rocks = new HashMap<>();
    for (Long r : originalRocks) {
      this.newRock(rocks, r, 1L);
    }
    for (int i = 0; i < timesToBlink; i++) {
      rocks = blink(rocks);
    }
    return rocks.values().stream().mapToLong(Long::longValue).sum();
  }

  private Map<Long, Long> blink(Map<Long, Long> rocks) {
    Map<Long, Long> nextRocks = new HashMap<>();
    for (Map.Entry<Long, Long> rockCounter : rocks.entrySet()) {
      Long rock = rockCounter.getKey();
      Long times = rockCounter.getValue();
      if (rock==0) {
        newRock(nextRocks, 1L, times);
      } else if (rock.toString().length() % 2==0) {
        int middle = rock.toString().length() / 2;
        newRock(nextRocks, (Long.parseLong(rock.toString().substring(0, middle))), times);
        newRock(nextRocks, (Long.parseLong(rock.toString().substring(middle))), times);
      } else {
        newRock(nextRocks, rock * 2024, times);
      }
    }

    return nextRocks;
  }

  private void newRock(Map<Long, Long> rocks, Long rock, Long times) {
    if (rocks.containsKey(rock)) {
      rocks.put(rock, rocks.get(rock) + times);
    } else {
      rocks.put(rock, times);
    }
  }

  public static void main(String[] args) {
    Day.run(Day11::new, "2024/D11_small.txt", "2024/D11_full.txt");
  }
}
