package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;

import java.util.Arrays;
import java.util.List;

public class Day15 extends Day {

  private static final String BOX = "O";
  private static final String ROBOT = "@";
  private static final String WALL = "#";
  private static final String EMPTY = ".";

  private enum Direction {
    UP("^"),
    DOWN("v"),
    RIGHT(">"),
    LEFT("<");

    private final String directionCode;

    Direction(String code) {
      this.directionCode = code;
    }

    public static Direction of(String letter) {
      for (Direction dir : Direction.values()) {
        if (dir.directionCode.equalsIgnoreCase(letter)) {
          return dir;
        }
      }
      throw new IllegalArgumentException();
    }

    public Point move(Point p) {
      return switch (this) {
        case UP -> Point.of(p.x - 1, p.y);
        case DOWN -> Point.of(p.x + 1, p.y);
        case LEFT -> Point.of(p.x, p.y - 1);
        case RIGHT -> Point.of(p.x, p.y + 1);
      };
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    String[] input = fileContents.split(System.lineSeparator() + System.lineSeparator());

    String[][] map = MatrixUtils.readMatrixFromFile(input[0]);
    List<Direction> orders = Arrays.stream(input[1].replaceAll(System.lineSeparator(), "").split("")).map(Direction::of).toList();

    Point r1 = findRobotPosition(map);

    for (Direction dir : orders) {
      //printMap(map, r1);
      Point r2 = dir.move(r1);
      if (EMPTY.equals(map[r2.x][r2.y])) {
        r1 = r2;
      } else if (BOX.equals(map[r2.x][r2.y])) {
        r1 = canBeBoxPushed(map, r2, dir) ? r2:r1;
      }
    }
    return String.valueOf(getBoxesCoordinates(map));
  }

  private boolean canBeBoxPushed(String[][] map, Point r2, Direction dir) {
    final Point r3 = dir.move(r2);

    if (!isPointWithinMapLimits(map, r3)) {
      return false;
    }

    boolean moved = switch (map[r3.x][r3.y]) {
      case EMPTY -> true;
      case BOX -> canBeBoxPushed(map, r3, dir);
      default -> false;
    };

    if (moved){
      map[r2.x][r2.y] = EMPTY;
      map[r3.x][r3.y] = BOX;
    }
    return moved;
  }

  private boolean isPointWithinMapLimits(String[][] map, Point p) {
    return p.x >= 0 && p.x < map.length && p.y >= 0 && p.y < map[0].length;
  }

  private Point findRobotPosition(String[][] map) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        if (ROBOT.equals(map[x][y])) {
          map[x][y] = EMPTY;
          return Point.of(x, y);
        }
      }
    }
    return null;
  }

  private long getBoxesCoordinates(String[][] map) {
    long total = 0L;
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        if (BOX.equals(map[x][y])) {
          total += x * 100L + y;
        }
      }
    }
    return total;
  }

  private void printMap(String[][] map, Point robot){
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        System.out.print(Point.of(x, y).equals(robot) ? ROBOT : map[x][y]);
      }
      System.out.println();
    }
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day15::new, "2024/D15_small.txt", "2024/D15_full.txt");
  }
}
