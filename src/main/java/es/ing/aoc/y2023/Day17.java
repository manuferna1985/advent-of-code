package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.CustomGraph;
import es.ing.aoc.common.dijkstra.CustomNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.ing.aoc.common.dijkstra.CustomNode.Direction.E;
import static es.ing.aoc.common.dijkstra.CustomNode.Direction.N;
import static es.ing.aoc.common.dijkstra.CustomNode.Direction.S;
import static es.ing.aoc.common.dijkstra.CustomNode.Direction.W;

public class Day17 extends Day {

  @Override
  protected String part1(String fileContents) throws Exception {
    Integer[][] map = MatrixUtils.readMatrixFromFile(fileContents, Integer.class, Integer::parseInt);
    Map<CustomNode, List<CustomNode>> nodes = buildGraphForNormalCrucible(map);
    CustomNode origin = new CustomNode(Point.of(0, 0), S, 0, 0); // S/E

    CustomGraph graph = new CustomGraph(nodes);
    graph.algorithm(origin);

    int[][] destDistances = graph.getDistances()[map.length - 1][map[0].length - 1];
    return String.valueOf(Arrays.stream(destDistances).flatMapToInt(Arrays::stream).min().orElse(0));
  }

  @Override
  protected String part2(String fileContents) throws Exception {
    Integer[][] map = MatrixUtils.readMatrixFromFile(fileContents, Integer.class, Integer::parseInt);
    Map<CustomNode, List<CustomNode>> nodes = buildGraphForUltraCrucible(map);
    CustomNode origin = new CustomNode(Point.of(0, 0), S, 1, 0); // S/E

    CustomGraph graph = new CustomGraph(nodes);
    graph.algorithm(origin);

    int[][] destDistances = graph.getDistances()[map.length - 1][map[0].length - 1];
    return String.valueOf(Arrays.stream(destDistances).flatMapToInt(Arrays::stream).min().orElse(0));
  }

  private Map<CustomNode, List<CustomNode>> buildGraphForNormalCrucible(Integer[][] map) {
    Map<CustomNode, List<CustomNode>> nodes = new HashMap<>();
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        for (CustomNode.Direction dir : CustomNode.Direction.values()) {
          for (int n=1; n<=3; n++){
            CustomNode node = new CustomNode(Point.of(x, y), dir, n, map[x][y]);
            List<CustomNode> neighbours = new ArrayList<>();

            if (x > 0 && !S.equals(dir)){
              // N (x-1, y)
              int steps = N.equals(dir) ? n + 1 : 1;
              if (steps <= 3){
                neighbours.add(new CustomNode(Point.of(x-1, y), N, steps, map[x-1][y]));
              }
            }

            if (x < map.length - 1 && !N.equals(dir)){
              // S (x+1, y)
              int steps = S.equals(dir) ? n + 1 : 1;
              if (steps <= 3){
                neighbours.add(new CustomNode(Point.of(x+1, y), S, steps, map[x+1][y]));
              }
            }

            if (y > 0 && !E.equals(dir)){
              // W (x, y-1)
              int steps = W.equals(dir) ? n + 1 : 1;
              if (steps <= 3){
                neighbours.add(new CustomNode(Point.of(x, y-1), W, steps, map[x][y-1]));
              }
            }

            if (y < map[x].length - 1 && !W.equals(dir)){
              // E (x, y+1)
              int steps = E.equals(dir) ? n + 1 : 1;
              if (steps <= 3){
                neighbours.add(new CustomNode(Point.of(x, y+1), E, steps, map[x][y+1]));
              }
            }

            nodes.put(node, neighbours);
          }
        }
      }
    }

    return nodes;
  }

  private Map<CustomNode, List<CustomNode>> buildGraphForUltraCrucible(Integer[][] map) {
    Map<CustomNode, List<CustomNode>> nodes = new HashMap<>();
    for (int x = 0; x < map.length; x++) {
      for (int y = 0; y < map[x].length; y++) {
        for (CustomNode.Direction dir : CustomNode.Direction.values()) {
          for (int n=1; n<=10; n++){
            CustomNode node = new CustomNode(Point.of(x, y), dir, n, map[x][y]);
            List<CustomNode> neighbours = new ArrayList<>();

            if (x > 0 && !S.equals(dir)){
              // N (x-1, y)
              int steps = N.equals(dir) ? n + 1 : 1;
              if (steps <= 10 && (N.equals(dir) || n >= 4)){
                neighbours.add(new CustomNode(Point.of(x-1, y), N, steps, map[x-1][y]));
              }
            }

            if (x < map.length - 1 && !N.equals(dir)){
              // S (x+1, y)
              int steps = S.equals(dir) ? n + 1 : 1;
              if (steps <= 10 && (S.equals(dir) || n >= 4)){
                neighbours.add(new CustomNode(Point.of(x+1, y), S, steps, map[x+1][y]));
              }
            }

            if (y > 0 && !E.equals(dir)){
              // W (x, y-1)
              int steps = W.equals(dir) ? n + 1 : 1;
              if (steps <= 10 && (W.equals(dir) || n >= 4)){
                neighbours.add(new CustomNode(Point.of(x, y-1), W, steps, map[x][y-1]));
              }
            }

            if (y < map[x].length - 1 && !W.equals(dir)){
              // E (x, y+1)
              int steps = E.equals(dir) ? n + 1 : 1;
              if (steps <= 10 && (E.equals(dir) || n >= 4)){
                neighbours.add(new CustomNode(Point.of(x, y+1), E, steps, map[x][y+1]));
              }
            }

            nodes.put(node, neighbours);
          }
        }
      }
    }

    return nodes;
  }

  public static void main(String[] args) {
    Day.run(Day17::new, "2023/D17_full.txt"); //, "2023/D17_full.txt");
  }
}
