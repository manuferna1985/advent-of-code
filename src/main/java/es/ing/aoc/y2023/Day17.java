package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.Graph;
import es.ing.aoc.common.dijkstra.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day17 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {

    Integer[][] map = MatrixUtils.readMatrixFromFile(fileContents, Integer.class, Integer::parseInt);
    Map<Integer, List<Node>> nodes = buildNodesFrom(map);

    Point origin = Point.of(0, 0);
    Point dest = Point.of(map.length - 1, map[0].length - 1);

    Graph graph = new Graph(nodes);
    graph.algorithm(getPointId(map, origin));
    return String.valueOf(graph.getDistances()[getPointId(map, dest)]);
  }

  private Map<Integer, List<Node>> buildNodesFrom(Integer[][] map) {
    Map<Integer, List<Node>> nodes = new HashMap<>();
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        nodes.put(
            getPointId(map, Point.of(x, y)),
            MatrixUtils.getNeighbours(map, x, y, false)
                .stream()
                .map(pair -> new Node(getPointId(map, pair.getKey()), pair.getRight()))
                .toList());
      }
    }
    return nodes;
  }

  private int getPointId(Integer[][] map, Point p) {
    return p.x * map.length + p.y;
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[] lines = fileContents.split(System.lineSeparator());


    return String.valueOf("");
  }

  public static void main(String[] args) {
    Day.run(Day17::new, "2023/D17_small.txt");//, "2023/D17_full.txt");
  }
}
