package es.ing.aoc.y2023;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.FloatPoint;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.dijkstra.Graph;
import es.ing.aoc.common.dijkstra.Node;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static es.ing.aoc.y2023.Day10.Pipe.*;

public class Day10 extends Day {

  private static final Integer LEFT = 0;
  private static final Integer RIGHT = 1;
  private static final Integer TOP = 2;
  private static final Integer BOTTOM = 3;

  public enum Pipe {
    START('S', 1, 1, 1, 1),
    GROUND('.', 0, 0, 0, 0),
    VERTICAL('|', 0, 0, 1, 1),
    HORIZONTAL('-', 1, 1, 0, 0),
    TURN_90_L('L', 0, 1, 1, 0),
    TURN_90_J('J', 1, 0, 1, 0),
    TURN_90_7('7', 1, 0, 0, 1),
    TURN_90_F('F', 0, 1, 0, 1);

    private final char letter;
    private final int[] connections;

    Pipe(char letter, int left, int right, int top, int bottom) {
      this.letter = letter;
      connections = new int[]{left, right, top, bottom};
    }

    public static Pipe of(char letter) {
      for (Pipe p : Pipe.values()) {
        if (p.letter==letter) {
          return p;
        }
      }
      throw new RuntimeException("Letter not found: " + letter);
    }

    public static Pipe of(String letter) {
      return Pipe.of(letter.charAt(0));
    }

    public boolean isConnected(int dir) {
      return connections[dir]==1;
    }
  }

  @Override
  protected String part1(String fileContents) throws Exception {

    String[][] matrix = MatrixUtils.readMatrixFromFile(fileContents);
    Pipe[][] ground = transformMatrix(matrix);
    Point s = this.findStart(ground);
    List<Point> validNeighbours = getValidNeighbours(matrix, ground, s);

    int maxSteps = 0;
    if (validNeighbours.size()==2) {
      // No backtracking is needed (part1)
      ground[s.x][s.y] = getRealStart(validNeighbours, ground, s);
      maxSteps = calculateSteps(matrix, ground, s);
    }

    return String.valueOf(maxSteps);
  }

  @Override
  protected String part2(String fileContents) throws Exception {

    String[][] matrixOrig = MatrixUtils.readMatrixFromFile(fileContents);
    String[][] matrix = new String[matrixOrig.length + 2][matrixOrig[0].length + 2];

    for (String[] strings : matrix) {
      Arrays.fill(strings, String.valueOf(GROUND.letter));
    }

    for (int x = 0; x < matrixOrig.length; x++) {
      System.arraycopy(matrixOrig[x], 0, matrix[x + 1], 1, matrixOrig[x].length);
    }

    Pipe[][] ground = transformMatrix(matrix);
    Point s = this.findStart(ground);

    Set<Point> path = new HashSet<>();
    List<Point> pending = new ArrayList<>();
    pending.add(s);

    while (!pending.isEmpty()) {
      Point next = pending.remove(0);
      if (!path.contains(next)) {
        path.add(next);
        pending.addAll(getValidNeighbours(matrix, ground, next));
      }
    }

    Set<FloatPoint> flooded = new HashSet<>();
    List<FloatPoint> pendingWater = new ArrayList<>();
    pendingWater.add(FloatPoint.of(0.5f, 0.5f));

    while (!pendingWater.isEmpty()) {
      FloatPoint next = pendingWater.remove(0);
      if (!flooded.contains(next)) {
        flooded.add(next);


        List<FloatPoint> positions = getAdjacentPositions(ground, next);
        for (FloatPoint pos : positions) {
          if (isGroundEmpty(path, pos) && isPathOpen(ground, pos)) {
            pendingWater.add(pos);
          }
        }
      }
    }

    List<Point> enclosedPoints = new ArrayList<>();
    for (int x = 0; x < ground.length; x++) {
      for (int y = 0; y < ground[x].length; y++) {
        if (!path.contains(Point.of(x, y)) && !flooded.contains(FloatPoint.of(x, y))) {
          enclosedPoints.add(Point.of(x, y));
        }
      }
    }

    return String.valueOf(enclosedPoints.size());
  }

  private boolean isPathOpen(Pipe[][] ground, FloatPoint pos) {

    if ((int) pos.x!=pos.x && (int) pos.y==pos.y) {
      int xA = (int) (pos.x - 0.5);
      int xB = (int) (pos.x + 0.5);
      if (ground[xA][(int) pos.y].isConnected(BOTTOM) && ground[xB][(int) pos.y].isConnected(TOP)) {
        return false;
      }
    }

    if ((int) pos.x==pos.x && (int) pos.y!=pos.y) {
      int yA = (int) (pos.y - 0.5);
      int yB = (int) (pos.y + 0.5);
      if (ground[(int) pos.x][yA].isConnected(RIGHT) && ground[(int) pos.x][yB].isConnected(LEFT)) {
        return false;
      }
    }

    return true;
  }

  private boolean isGroundEmpty(Set<Point> pipesPath, FloatPoint fp) {
    if (!fp.isIntegerPoint()) {
      return true;
    } else {
      Point p = fp.getIntegerPoint();
      return !pipesPath.contains(p);
    }
  }

  private List<FloatPoint> getAdjacentPositions(Pipe[][] ground, FloatPoint next) {
    List<FloatPoint> positions = new ArrayList<>();
    float x = next.x, y = next.y;

    if (x > 0) positions.add(FloatPoint.of(x - 0.5f, y));
    if (x < ground.length - 1) positions.add(FloatPoint.of(x + 0.5f, y));
    if (y > 0) positions.add(FloatPoint.of(x, y - 0.5f));
    if (y < ground[0].length - 1) positions.add(FloatPoint.of(x, y + 0.5f));

    return positions;
  }

  private static List<Point> getValidNeighbours(String[][] matrix, Pipe[][] ground, Point s) {
    List<Pair<Point, String>> neighbours = MatrixUtils.getNeighbours(matrix, s.x, s.y, false);
    List<Point> validNeighbours = new ArrayList<>();
    Pipe sPipe = ground[s.x][s.y];

    for (Pair<Point, String> n : neighbours) {
      Point nPoint = n.getLeft();
      Pipe nPipe = ground[nPoint.x][nPoint.y];
      if (nPoint.x==s.x - 1 && nPipe.isConnected(BOTTOM) && sPipe.isConnected(TOP)) {
        validNeighbours.add(nPoint);
      } else if (nPoint.x==s.x + 1 && nPipe.isConnected(TOP) && sPipe.isConnected(BOTTOM)) {
        validNeighbours.add(nPoint);
      } else if (nPoint.y==s.y - 1 && nPipe.isConnected(RIGHT) && sPipe.isConnected(LEFT)) {
        validNeighbours.add(nPoint);
      } else if (nPoint.y==s.y + 1 && nPipe.isConnected(LEFT) && sPipe.isConnected(RIGHT)) {
        validNeighbours.add(nPoint);
      }
    }
    return validNeighbours;
  }

  private Point findStart(Pipe[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (START.equals(matrix[i][j])) {
          return new Point(i, j);
        }
      }
    }
    throw new RuntimeException("S not found in map!");
  }

  private Pipe[][] transformMatrix(String[][] matrix) {
    Pipe[][] result = new Pipe[matrix.length][matrix[0].length];
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        result[i][j] = Pipe.of(matrix[i][j]);
      }
    }
    return result;
  }

  private Pipe getRealStart(List<Point> validNeighbours, Pipe[][] ground, Point s) {
    Point n1 = validNeighbours.get(0);
    Pipe n1Pipe = ground[n1.x][n1.y];
    Point n2 = validNeighbours.get(1);
    Pipe n2Pipe = ground[n2.x][n2.y];

    if (n1Pipe.equals(n2Pipe)) {
      return Pipe.of(n1Pipe.letter);
    } else if (n1.x==s.x - 1) {
      if (n2.y==s.y - 1) {
        return TURN_90_J;
      } else {
        return TURN_90_L;
      }
    } else {
      if (n2.y==s.y - 1) {
        return TURN_90_7;
      } else {
        return TURN_90_F;
      }
    }
  }


  private Integer calculateSteps(String[][] matrix, Pipe[][] ground, Point start) {

    Map<Integer, List<Node>> edges = new HashMap<>();

    Map<Point, Integer> pointIds = new HashMap<>();
    int counter = 0;
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        pointIds.put(Point.of(x, y), counter++);
      }
    }

    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        Point s = Point.of(x, y);
        int sId = pointIds.get(s);
        List<Point> points = getValidNeighbours(matrix, ground, s);
        if (!points.isEmpty()) {
          List<Node> nodes = points.stream().map(p -> new Node(pointIds.get(p), 1)).toList();
          edges.put(sId, nodes);
        } else {
          edges.put(sId, Collections.emptyList());
        }
      }
    }

    Graph graph = new Graph(edges);

    int[] shortestPaths = graph.algorithm(pointIds.get(start)).getDistances();

    return Arrays.stream(shortestPaths)
        .filter(length -> length < Integer.MAX_VALUE)
        .max().orElse(Integer.MAX_VALUE);
  }

  public static void main(String[] args) {
    Day.run(Day10::new, "2023/D10_small6.txt", "2023/D10_full.txt");
  }
}
