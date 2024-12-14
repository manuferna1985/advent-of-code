package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day14 extends Day {

  record Robot(Point pos, Point vel) {
    public static Robot of(String line) {
      String[] parts = line.split(" ");
      List<Integer> rawPos = Arrays.stream(parts[0].substring(2).split(",")).map(Integer::parseInt).toList();
      List<Integer> rawVel = Arrays.stream(parts[1].substring(2).split(",")).map(Integer::parseInt).toList();
      return new Robot(Point.of(rawPos.get(0), rawPos.get(1)), Point.of(rawVel.get(0), rawVel.get(1)));
    }

    public void move(int maxX, int maxY) {
      pos.x += vel.x;
      pos.x = pos.x < 0 ? pos.x + maxX:pos.x;
      pos.y += vel.y;
      pos.y = pos.y < 0 ? pos.y + maxY:pos.y;
      pos.x %= maxX;
      pos.y %= maxY;
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {
    final List<Robot> robots = readRobots(fileContents);
    final int maxWidth = getMaxWidth(robots);
    final int maxHeight = getMaxHeight(robots);

    for (int i = 0; i < 100; i++) {
      for (Robot r : robots) {
        r.move(maxWidth, maxHeight);
      }
    }

    return String.valueOf(
        getCuadrantsPredicates(maxWidth, maxHeight).stream()
            .map(pred -> robots.stream().filter(pred).count())
            .mapToLong(Long::longValue)
            .reduce(1, (a, b) -> a * b));
  }

  private static List<Predicate<Robot>> getCuadrantsPredicates(int maxWidth, int maxHeight) {
    int midWidth = maxWidth / 2;
    int midHeight = maxHeight / 2;
    return List.of(
        (robot -> robot.pos.x < midWidth && robot.pos.y < midHeight),
        (robot -> robot.pos.x > midWidth && robot.pos.y < midHeight),
        (robot -> robot.pos.x < midWidth && robot.pos.y > midHeight),
        (robot -> robot.pos.x > midWidth && robot.pos.y > midHeight));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    final List<Robot> robots = readRobots(fileContents);
    final int maxWidth = getMaxWidth(robots);
    final int maxHeight = getMaxHeight(robots);

    int seconds = 0;
    while (true) {
      Map<Point, Integer> map = new HashMap<>();
      // We move all robots, counting how many are in each position after they have moved
      for (Robot r : robots) {
        r.move(maxWidth, maxHeight);
        map.put(r.pos, map.containsKey(r.pos) ? map.get(r.pos) + 1:1);
      }
      // If there's just one robot in each map position (no one is sharing the same position)
      if (map.values().stream().allMatch(n -> n==1)) {
        printRobots(robots, maxWidth, maxHeight);
        seconds++;
        break;
      }
      seconds++;
    }

    return String.valueOf(seconds);
  }

  private static int getMaxHeight(List<Robot> robots) {
    return robots.size() > 15 ? 103:7;
  }

  private static int getMaxWidth(List<Robot> robots) {
    return robots.size() > 15 ? 101:11;
  }

  private void printRobots(List<Robot> robots, int maxWidth, int maxHeight) {
    List<Point> positions = robots.stream().map(r -> r.pos).toList();
    for (int h = 0; h < maxHeight; h++) {
      for (int w = 0; w < maxWidth; w++) {
        System.out.print(positions.contains(Point.of(w, h)) ? "X":".");
      }
      System.out.println();
    }
  }

  private List<Robot> readRobots(String fileContents) {
    return Arrays.stream(fileContents.split(System.lineSeparator())).map(Robot::of).toList();
  }

  public static void main(String[] args) {
    Day.run(Day14::new, "2024/D14_small.txt", "2024/D14_full.txt");
  }
}
