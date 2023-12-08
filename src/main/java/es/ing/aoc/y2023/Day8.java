package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MathUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static es.ing.aoc.y2023.Day8.Direction.LEFT;

public class Day8 extends Day {

  enum Direction {
    LEFT, RIGHT;

    public static Direction of(char letter) {
      for (Direction c : Direction.values()) {
        if (c.name().charAt(0)==letter) {
          return c;
        }
      }
      throw new RuntimeException("Direction not found!!!");
    }
  }

  record Path(List<Direction> directions) {
  }

  record Decision(String left, String right) {
    String go(Direction dir) {
      return LEFT.equals(dir) ? left:right;
    }
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

    Path path = new Path(lines[0].chars().mapToObj(i -> Direction.of((char) i)).toList());
    Map<String, Decision> decisions = new HashMap<>();

    for (int i = 2; i < lines.length; i++) {
      List<String> parts = Arrays.stream(lines[i].split("[ =(,)]")).filter(StringUtils::isNotEmpty).toList();
      decisions.put(parts.get(0), new Decision(parts.get(1), parts.get(2)));
    }

    return decisions.keySet().stream()
        .filter(startCondition)
        .map(start -> getStepsToReachDestination(path, start, decisions, endCondition))
        .reduce(MathUtils::mcm).orElse(0L);
  }

  private long getStepsToReachDestination(Path path, String start, Map<String, Decision> decisions, Predicate<String> condition) {

    boolean end = false;
    long steps = 0;

    String current = start;
    while (!end) {
      Direction dir = path.directions.get((int) (steps % path.directions.size()));
      current = decisions.get(current).go(dir);
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
