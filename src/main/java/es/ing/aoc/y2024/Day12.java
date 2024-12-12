package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Day12 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] garden = MatrixUtils.readMatrixFromFile(fileContents);

    List<List<Point>> plants = new ArrayList<>();
    List<Point> visited = new ArrayList<>();

    for (int i = 0; i < garden.length; i++) {
      for (int j = 0; j < garden[i].length; j++) {
        Point p = Point.of(i, j);
        if (!visited.contains(p)) {
          List<Point> group = new ArrayList<>();
          buildGroup(garden, i, j, group, visited);
          plants.add(group);
        }
      }
    }

    return String.valueOf(
        plants.stream()
            .map(group -> this.getArea(group) * this.getPerimeter(garden, group))
            .mapToInt(Integer::intValue)
            .sum());
  }

  private void buildGroup(String[][] garden, int x, int y, List<Point> group, List<Point> visited) {
    Point p = Point.of(x, y);
    group.add(p);
    visited.add(p);

    MatrixUtils.getNeighbours(garden, x, y, false)
        .stream()
        .filter(n -> n.getValue().equals(garden[x][y]))
        .filter(n -> !group.contains(n.getKey()))
        .forEach(n -> buildGroup(garden, n.getKey().x, n.getKey().y, group, visited));
  }

  private int getPerimeter(String[][] garden, List<Point> group) {
    int perimeter = 0;
    for (Point p : group) {
      List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(garden, p.x, p.y, false);
      for (Pair<Point, String> n : neighbours) {
        if (!group.contains(n.getKey())) {
          perimeter++;
        }
      }
      if (p.x == 0 || p.x == garden.length - 1){
        perimeter++;
      }
      if (p.y == 0 || p.y == garden[0].length - 1){
        perimeter++;
      }
    }
    return perimeter;
  }

  private int getArea(List<Point> group) {
    return group.size();
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[] lines = fileContents.split(System.lineSeparator());

    return "";
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2024/D12_small.txt", "2024/D12_full.txt");
  }
}
