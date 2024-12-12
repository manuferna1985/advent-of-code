package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.FloatPoint;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day12 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] garden = MatrixUtils.readMatrixFromFile(fileContents);
    List<List<Point>> plants = buildPlants(garden);
    return String.valueOf(
        plants.stream()
            .map(group -> this.getArea(group) * this.getNormalPerimeter(garden, group))
            .mapToInt(Integer::intValue)
            .sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] garden = MatrixUtils.readMatrixFromFile(fileContents);
    List<List<Point>> plants = buildPlants(garden);
    return String.valueOf(
        plants.stream()
            .map(group -> this.getArea(group) * this.getPerimeterStraightLines(garden, group))
            .mapToInt(Integer::intValue)
            .sum());
  }

  private List<List<Point>> buildPlants(String[][] garden) {

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
    return plants;
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

  private int getNormalPerimeter(String[][] garden, List<Point> group) {
    return getPlantGroupFences(garden, group).size();
  }

  private int getPerimeterStraightLines(String[][] garden, List<Point> group) {
    List<FloatPoint> fences = getPlantGroupFences(garden, group);
    List<List<FloatPoint>> groupedFence = new ArrayList<>();

    boolean changed;

    while (!fences.isEmpty()) {
      changed = false;

      List<FloatPoint> remainingFences = new ArrayList<>();
      for (FloatPoint p : fences) {
        Optional<List<FloatPoint>> matchingFenceGroup = groupedFence.stream().filter(fenceGroup -> fenceGroup.stream()
                .anyMatch(p2 -> isStraightLine(garden, p2, p)))
            .findFirst();

        if (matchingFenceGroup.isPresent()) {
          matchingFenceGroup.get().add(p);
          changed = true;
        } else {
          if (!changed) {
            List<FloatPoint> newGroup = new ArrayList<>();
            newGroup.add(p);
            groupedFence.add(newGroup);
            changed = true;
          } else {
            remainingFences.add(p);
          }
        }
      }
      fences = remainingFences;
    }

    return groupedFence.size();
  }

  private List<FloatPoint> getPlantGroupFences(String[][] garden, List<Point> group) {
    List<FloatPoint> fences = new ArrayList<>();
    for (Point p : group) {
      List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(garden, p.x, p.y, false);
      for (Pair<Point, String> n : neighbours) {
        if (!group.contains(n.getKey())) {
          fences.add(middle(p, n.getKey()));
        }
      }
      if (p.x==0 || p.x==garden.length - 1) {
        fences.add(middle(p, Point.of(p.x==0 ? p.x - 1:p.x + 1, p.y)));
      }
      if (p.y==0 || p.y==garden[0].length - 1) {
        fences.add(middle(p, Point.of(p.x, p.y==0 ? p.y - 1:p.y + 1)));
      }
    }
    return fences;
  }

  private boolean isStraightLine(String[][] garden, FloatPoint p1, FloatPoint p2) {
    if (p1.x==p2.x && isFloatingPoint(p1.x)) {
      // p1 and p2 are both horizontal fences and adjacent between them
      return Math.abs(p2.y - p1.y)==1 && isDivisorLine(garden, p1, p2);
    }
    if (p1.y==p2.y && isFloatingPoint(p1.y)) {
      // p1 and p2 are both vertical fences and adjacent between them
      return Math.abs(p2.x - p1.x)==1 && isDivisorLine(garden, p1, p2);
    }
    return false;
  }

  private boolean isDivisorLine(String[][] garden, FloatPoint p1, FloatPoint p2) {

    if (p1.x==p2.x) {
      // Horizontal
      // A A      ? ?
      // ---  or  ---
      // ? ?      A A
      if (p1.x > 0 && p1.x < garden.length) {
        return garden[(int) Math.floor(p1.x)][(int) p1.y].equals(garden[(int) Math.floor(p1.x)][(int) p2.y])
            || garden[(int) Math.ceil(p1.x)][(int) p1.y].equals(garden[(int) Math.ceil(p1.x)][(int) p2.y]);
      }
    } else {
      // Vertical
      // A | ?  or  ? | A
      // A | ?      ? | A
      if (p1.y > 0 && p1.y < garden[0].length) {
        return garden[(int) p1.x][(int) Math.floor(p1.y)].equals(garden[(int) p2.x][(int) Math.floor(p1.y)])
            || garden[(int) p1.x][(int) Math.ceil(p1.y)].equals(garden[(int) p2.x][(int) Math.ceil(p1.y)]);
      }
    }
    return true;
  }

  private FloatPoint middle(Point a, Point b) {
    // Calculates the virtual coordinates of a fence between two real and integer points
    return FloatPoint.of((a.x + b.x) / 2.0f, (a.y + b.y) / 2.0f);
  }

  private boolean isFloatingPoint(float number) {
    // For checking fences (real floating numbers)
    return Math.floor(number)!=number;
  }

  private int getArea(List<Point> group) {
    return group.size();
  }

  public static void main(String[] args) {
    Day.run(Day12::new, "2024/D12_small.txt", "2024/D12_full.txt");
  }
}
