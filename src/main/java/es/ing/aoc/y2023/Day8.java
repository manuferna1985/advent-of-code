package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static es.ing.aoc.y2023.Day8.Direction.LEFT;

public class Day8 extends Day {

  enum Direction {
    LEFT, RIGHT;
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    return String.valueOf(getTotalSteps(fileContents, node -> node.equals("AAA"), node -> node.equals("ZZZ")));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return String.valueOf(getTotalSteps(fileContents, node -> node.endsWith("A"), node -> node.endsWith("Z")));
  }

  private long getTotalSteps(String fileContents, Predicate<String> startCondition, Predicate<String> endCondition) {

    String[] lines = fileContents.split(System.lineSeparator());

    List<Direction> path = lines[0].chars().mapToObj(c -> (c=='L') ? LEFT:Direction.RIGHT).toList();
    Map<String, Pair<String, String>> decisions = new HashMap<>();

    for (int i = 2; i < lines.length; i++) {
      List<String> parts = Arrays.stream(lines[i].split("[ =(,)]")).filter(StringUtils::isNotEmpty).toList();
      decisions.put(parts.get(0), Pair.of(parts.get(1), parts.get(2)));
    }

    return decisions.keySet().stream()
        .filter(startCondition)
        .map(start -> getStepsToReachDestination(path, start, decisions, endCondition))
        .reduce(MathUtils::mcm).orElse(0L);
  }

  private long getStepsToReachDestination(List<Direction> path, String start, Map<String, Pair<String, String>> decisions, Predicate<String> condition) {

    boolean end = false;
    long steps = 0;

    String current = start;
    while (!end) {
      Direction dir = path.get((int) (steps % path.size()));
      current = LEFT.equals(dir) ? decisions.get(current).getLeft():decisions.get(current).getRight();
      steps++;

      if (condition.test(current)) {
        end = true;
      }
    }

    return steps;
  }

  public static void main(String[] args) {
    Day.run(Day8::new, "2023/D8_small.txt", "2023/D8_full.txt");
  }
}
