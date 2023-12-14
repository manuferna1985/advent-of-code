package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Day {


  @Override
  protected String part1(String fileContents) throws Exception {

    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);

    List<Point> rocks = new ArrayList<>();
    List<Point> walls = new ArrayList<>();
    List<Point> map = new ArrayList<>();

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        switch (matrix[x][y]) {
          case "O" -> rocks.add(Point.of(x, y));
          case "#" -> walls.add(Point.of(x, y));
        }
      }
    }

    map.addAll(rocks);
    map.addAll(walls);

    for (Point rock : rocks) {
      while (rock.x > 0 && !map.contains(Point.of(rock.x - 1, rock.y))) {
        rock.x--;
      }
    }

    final int maxRows = matrix.length;
    return String.valueOf(rocks.stream().mapToInt(p -> maxRows - p.x).sum());
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());


    return String.valueOf("");
  }


  public static void main(String[] args) {
    Day.run(Day14::new, "2023/D14_small.txt", "2023/D14_full.txt");
  }
}
