package es.ing.aoc.y2024;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Day10 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    return getScore(fileContents, false);
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    return getScore(fileContents, true);
  }

  private String getScore(String fileContents, boolean allowRepeatedPaths){
    Integer[][] map = MatrixUtils.readMatrixFromFile(fileContents, Integer.class, Integer::parseInt);
    return String.valueOf(
        searchGroundPoints(map).parallelStream()
            .map(gp -> algorithm(map, gp, allowRepeatedPaths))
            .mapToInt(Integer::intValue)
            .sum());
  }

  private List<Pair<Point, Integer>> searchGroundPoints(Integer[][] map){
    List<Pair<Point, Integer>> grounds = new ArrayList<>();
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[i].length; j++) {
        if (map[i][j]==0) {
          grounds.add(Pair.of(Point.of(i, j), 0));
        }
      }
    }
    return grounds;
  }

  private int algorithm(Integer[][] map, Pair<Point, Integer> p, boolean allowRepeatedPaths) {

    List<Pair<Point, Integer>> survivors = new ArrayList<>();
    survivors.add(p);

    final Map<Point, Collection<Point>> peaks = new HashMap<>();
    final List<Pair<Point, Integer>> next = new Vector<>();

    while (!survivors.isEmpty()) {

      survivors.parallelStream().forEach(p1 -> {
        if (p1.getValue() < 9) {
          List<Pair<Point, Integer>> neighbours = MatrixUtils.getNeighbours(map, p1.getKey().x, p1.getKey().y, false)
              .stream()
              .filter(p2 -> p2.getValue()==p1.getValue() + 1)
              .toList();

          if (!neighbours.isEmpty()) {
            next.addAll(neighbours);
          }
        } else {
          synchronized (peaks) {
            if (peaks.containsKey(p1.getKey())) {
              peaks.get(p1.getKey()).add(p1.getKey());
            } else {
              Collection<Point> endSet = allowRepeatedPaths ? new ArrayList<>() : new HashSet<>();
              endSet.add(p1.getKey());
              peaks.put(p1.getKey(), endSet);
            }
          }
        }
      });

      survivors.clear();
      survivors.addAll(next);
      next.clear();
    }

    return peaks.values().stream().mapToInt(Collection::size).sum();
  }

  public static void main(String[] args) {
    Day.run(Day10::new, "2024/D10_small.txt", "2024/D10_full.txt");
  }
}
