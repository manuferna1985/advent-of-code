package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 extends Day {

  private static final Integer GRID_SIZE = 1000;
  private static final String COORDS_SEP = ",";

  private enum Action {
    ON, OFF, TOGGLE;

    public static Action from(String instruction) {
      if (instruction.startsWith("turn on")) {
        return ON;
      } else if (instruction.startsWith("turn off")) {
        return OFF;
      } else {
        return TOGGLE;
      }
    }

    public static int execSimple(Action action, int currentValue) {
      return switch (action) {
        case ON -> 1;
        case OFF -> 0;
        case TOGGLE -> currentValue==1 ? 0:1;
      };
    }

    public static int execComplex(Action action, int currentValue) {
      return switch (action) {
        case ON -> currentValue + 1;
        case OFF -> Math.max(0, currentValue - 1);
        case TOGGLE -> currentValue + 2;
      };
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    List<Triple<Action, Point, Point>> instructions = getInstructions(fileContents);
    int[][] lights = new int[GRID_SIZE][GRID_SIZE];
    executeActions(lights, instructions, Action::execSimple);
    return String.valueOf(countEnabledLights(lights));
  }


  @Override
  protected String part2(String fileContents) throws Exception {
    List<Triple<Action, Point, Point>> instructions = getInstructions(fileContents);
    int[][] lights = new int[GRID_SIZE][GRID_SIZE];
    executeActions(lights, instructions, Action::execComplex);
    return String.valueOf(countEnabledLights(lights));
  }

  private List<Triple<Action, Point, Point>> getInstructions(String fileContents) {
    String[] lines = fileContents.split(System.lineSeparator());
    Pattern pattern = Pattern.compile("(\\d+,\\d+) .* (\\d+,\\d+)");
    Matcher matcher;

    List<Triple<Action, Point, Point>> coords = new ArrayList<>();
    for (String line : lines) {
      matcher = pattern.matcher(line);
      if (matcher.find()) {
        coords.add(Triple.of(
            Action.from(line),
            Point.of(matcher.group(1).split(COORDS_SEP)),
            Point.of(matcher.group(2).split(COORDS_SEP))));
      }
    }

    return coords;
  }

  private void executeActions(int[][] grid, List<Triple<Action, Point, Point>> instructions, BiFunction<Action, Integer, Integer> fn) {
    for (Triple<Action, Point, Point> inst : instructions) {
      for (int x = inst.getMiddle().x; x <= inst.getRight().x; x++) {
        for (int y = inst.getMiddle().y; y <= inst.getRight().y; y++) {
          grid[x][y] = fn.apply(inst.getLeft(), grid[x][y]);
        }
      }
    }
  }

  private int countEnabledLights(int[][] grid) {
    return Arrays.stream(grid).flatMapToInt(Arrays::stream).sum();
  }

  public static void main(String[] args) {
    Day.run(Day6::new, "2015/D6_small.txt", "2015/D6_full.txt");
  }
}
