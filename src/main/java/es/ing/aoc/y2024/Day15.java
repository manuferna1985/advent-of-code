package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static es.ing.aoc.y2024.Day15.Direction.LEFT;
import static es.ing.aoc.y2024.Day15.Direction.RIGHT;

public class Day15 extends Day {

  private static final String BOX = "O";
  private static final String BIG_BOX_LEFT = "[";
  private static final String BIG_BOX_RIGHT = "]";
  private static final String BIG_BOX = BIG_BOX_LEFT + BIG_BOX_RIGHT;
  private static final String ROBOT = "@";
  private static final String WALL = "#";
  private static final String EMPTY = ".";

  public enum Direction {
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

    public Pair<Point, Point> move(Pair<Point, Point> box) {
      return Pair.of(move(box.getLeft()), move(box.getRight()));
    }

    public boolean isHorizontal() {
      return LEFT.equals(this) || RIGHT.equals(this);
    }

    public boolean isVertical() {
      return UP.equals(this) || DOWN.equals(this);
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
        r1 = canBoxBePushed(map, r2, dir) ? r2:r1;
      }
    }
    //printMap(map, r1);
    return String.valueOf(getBoxesCoordinates(map));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] input = fileContents.split(System.lineSeparator() + System.lineSeparator());
    String expandedMap = input[0]
        .replace(WALL, WALL + WALL)
        .replace(BOX, BIG_BOX)
        .replace(EMPTY, EMPTY + EMPTY)
        .replace(ROBOT, ROBOT + EMPTY);

    String[][] map = MatrixUtils.readMatrixFromFile(expandedMap);
    List<Direction> orders = Arrays.stream(input[1].replaceAll(System.lineSeparator(), "").split("")).map(Direction::of).toList();

    Point r1 = findRobotPosition(map);
    for (Direction dir : orders) {
      //printMap(map, r1);
      Point r2 = dir.move(r1);
      if (EMPTY.equals(map[r2.x][r2.y])) {
        r1 = r2;
      } else if (BIG_BOX_LEFT.equals(map[r2.x][r2.y])) {
        r1 = canBigBoxBePushed(map, Pair.of(r2, RIGHT.move(r2)), dir) ? r2:r1;
      } else if (BIG_BOX_RIGHT.equals(map[r2.x][r2.y])) {
        r1 = canBigBoxBePushed(map, Pair.of(LEFT.move(r2), r2), dir) ? r2:r1;
      }
    }
    //printMap(map, r1);
    return String.valueOf(getBoxesCoordinates(map));
  }

  private boolean canBoxBePushed(String[][] map, Point r2, Direction dir) {
    final Point r3 = dir.move(r2);

    if (!isPointWithinMapLimits(map, r3)) {
      return false;
    }

    boolean moved = switch (map[r3.x][r3.y]) {
      case EMPTY -> true;
      case BOX -> canBoxBePushed(map, r3, dir);
      default -> false;
    };

    if (moved) {
      moveBox(map, r2, r3);
    }
    return moved;
  }

  private boolean canBigBoxBePushed(String[][] map, Pair<Point, Point> r2, Direction dir) {
    List<Pair<Point, Point>> moving = new ArrayList<>();
    boolean canMove = canBigBoxBePushed(map, r2, dir, moving);
    if (canMove) {
      for (Pair<Point, Point> box : moving) {
        this.clearBigBox(map, box);
      }
      for (Pair<Point, Point> box : moving) {
        this.moveBigBox(map, dir.move(box));
      }
    }
    return canMove;
  }

  private boolean canBigBoxBePushed(String[][] map, Pair<Point, Point> r2, Direction dir, List<Pair<Point, Point>> moving) {
    final Pair<Point, Point> r3 = Pair.of(dir.move(r2.getLeft()), dir.move(r2.getRight()));
    moving.add(r2);

    if (!isPointWithinMapLimits(map, r3.getLeft()) || !isPointWithinMapLimits(map, r2.getRight())) {
      return false;
    }
    if (dir.isHorizontal() && checkAnyMap(map, r3, EMPTY)) {
      return true;
    }
    if (dir.isVertical() && checkBothMap(map, r3, EMPTY)) {
      return true;
    }
    if (checkAnyMap(map, r3, WALL)) {
      return false;
    }

    if (dir.isHorizontal()) {
      // [][]: call recursive to check whether the other box can be pushed
      final Pair<Point, Point> r4 = Pair.of(dir.move(r3.getLeft()), dir.move(r3.getRight()));
      return canBigBoxBePushed(map, r4, dir, moving);
    } else {

      List<Pair<Point, Point>> boxesToMove = new ArrayList<>();
      if (checkMap(map, r3.getLeft(), map[r2.getLeft().x][r2.getLeft().y])) {
        // boxes are aligned vertically
        boxesToMove.add(r3);
      } else {
        // boxes not aligned vertically
        if (checkMap(map, r3.getRight(), BIG_BOX_LEFT)) {
          boxesToMove.add(RIGHT.move(r3));
        }
        if (checkMap(map, r3.getLeft(), BIG_BOX_RIGHT)) {
          boxesToMove.add(LEFT.move(r3));
        }
      }
      return boxesToMove.stream().allMatch(box -> canBigBoxBePushed(map, box, dir, moving));
    }
  }

  private void moveBox(String[][] map, Point p1, Point p2) {
    map[p1.x][p1.y] = EMPTY;
    map[p2.x][p2.y] = BOX;
  }

  private void clearBigBox(String[][] map, Pair<Point, Point> p) {
    map[p.getLeft().x][p.getLeft().y] = EMPTY;
    map[p.getRight().x][p.getRight().y] = EMPTY;
  }

  private void moveBigBox(String[][] map, Pair<Point, Point> p) {
    map[p.getLeft().x][p.getLeft().y] = BIG_BOX_LEFT;
    map[p.getRight().x][p.getRight().y] = BIG_BOX_RIGHT;
  }

  private boolean checkBothMap(String[][] map, Pair<Point, Point> p, String check) {
    return checkMap(map, p.getLeft(), check) && checkMap(map, p.getRight(), check);
  }

  private boolean checkAnyMap(String[][] map, Pair<Point, Point> p, String check) {
    return checkMap(map, p.getLeft(), check) || checkMap(map, p.getRight(), check);
  }

  private boolean checkMap(String[][] map, Point p, String check) {
    return check.equals(map[p.x][p.y]);
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
        if (BOX.equals(map[x][y]) || BIG_BOX_LEFT.equals(map[x][y])) {
          total += x * 100L + y;
        }
      }
    }
    return total;
  }

  private void printMap(String[][] map, Point robot) {
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        System.out.print(Point.of(x, y).equals(robot) ? ROBOT:map[x][y]);
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    Day.run(Day15::new, "2024/D15_small.txt", "2024/D15_full.txt");
  }
}
