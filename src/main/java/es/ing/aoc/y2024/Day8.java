package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    return String.valueOf(calculateAntinodes(buildAntennasFromMap(map), map, false).size());
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    String[][] map = MatrixUtils.readMatrixFromFile(fileContents);
    return String.valueOf(calculateAntinodes(buildAntennasFromMap(map), map, true).size());
  }

  private Set<Point> calculateAntinodes(Map<String, List<Point>> antennas, String[][] map, boolean evolvedAntinodes) {
    Set<Point> antinodes = new HashSet<>();

    for (var entry : antennas.entrySet()) {
      Generator.combination(entry.getValue()).simple(2).stream().forEach(comb ->
      {
        Point first = comb.get(0);
        Point second = comb.get(1);
        Point diffs = Point.of(second.x - first.x, second.y - first.y);

        if (evolvedAntinodes) {
          antinodes.add(first);
          antinodes.add(second);
        }

        Point antinode1 = Point.of(first.x - diffs.x, first.y - diffs.y);
        while (validAntinode(map, antinode1)) {
          antinodes.add(antinode1);
          antinode1 = Point.of(antinode1.x - diffs.x, antinode1.y - diffs.y);
          if (!evolvedAntinodes) {
            break;
          }
        }

        Point antinode2 = Point.of(second.x + diffs.x, second.y + diffs.y);
        while (validAntinode(map, antinode2)) {
          antinodes.add(antinode2);
          antinode2 = Point.of(antinode2.x + diffs.x, antinode2.y + diffs.y);
          if (!evolvedAntinodes) {
            break;
          }
        }
      });
    }
    return antinodes;
  }

  private Map<String, List<Point>> buildAntennasFromMap(String[][] map) {
    Map<String, List<Point>> antennas = new HashMap<>();
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        String key = map[i][j];
        if (!key.equals(".")) {
          Point newLoc = Point.of(i, j);
          if (antennas.containsKey(key)) {
            antennas.get(key).add(newLoc);
          } else {
            List<Point> newPointsList = new ArrayList<>();
            newPointsList.add(newLoc);
            antennas.put(key, newPointsList);
          }
        }
      }
    }
    return antennas;
  }

  private boolean validAntinode(String[][] map, Point ant) {
    return ant.x >= 0 && ant.x < map.length && ant.y >= 0 && ant.y < map[0].length;
  }

  public static void main(String[] args) {
    Day.run(Day8::new, "2024/D8_small.txt", "2024/D8_full.txt");
  }
}
