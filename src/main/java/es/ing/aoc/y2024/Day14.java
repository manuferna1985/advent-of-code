package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    String[] lines = fileContents.split(System.lineSeparator());

    final int maxWidth = lines.length > 15 ? 101:11;
    final int maxHeight = lines.length > 15 ? 103:7;

    List<Robot> robots = new ArrayList<>();

    for (String line : lines) {
      robots.add(Robot.of(line));
    }

    for (int i = 0; i < 100; i++) {
      for (Robot r : robots) {
        r.move(maxWidth, maxHeight);
      }
    }

    int midWidth = maxWidth / 2;
    int midHeight = maxHeight / 2;

    Predicate<Robot> first = (robot -> robot.pos.x < midWidth && robot.pos.y < midHeight);
    Predicate<Robot> second = (robot -> robot.pos.x > midWidth && robot.pos.y < midHeight);
    Predicate<Robot> third = (robot -> robot.pos.x < midWidth && robot.pos.y > midHeight);
    Predicate<Robot> fourth = (robot -> robot.pos.x > midWidth && robot.pos.y > midHeight);

    long count1 = robots.stream().filter(first).count();
    long count2 = robots.stream().filter(second).count();
    long count3 = robots.stream().filter(third).count();
    long count4 = robots.stream().filter(fourth).count();

    return String.valueOf(count1 * count2 * count3 * count4);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day14::new, "2024/D14_small.txt", "2024/D14_full.txt");
  }
}
