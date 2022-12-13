package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.dijkstra.Graph;
import es.ing.aoc.common.dijkstra.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Day12 extends Day {

    private static final int LOWER = 'a';
    private static final int HIGHER = 'z';
    private static final Byte WEIGHT = (byte) 1;

    static final class Point {
        private final Integer id;
        private final Integer elevation;

        public Point(Integer id, Integer elevation) {
            this.id = id;
            this.elevation = elevation;
        }

        public Integer getId() {
            return id;
        }

        public Integer getElevation() {
            return elevation;
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        return String.valueOf(processInput(fileContents, false));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return String.valueOf(processInput(fileContents, true));
    }

    private int processInput(String fileContents, boolean getMinFromLowerElevation) {
        List<String> allLines = Arrays.asList(fileContents.split(System.lineSeparator())); // when input file is multiline

        Map<Integer, List<Node>> neighbours = new HashMap<>();
        final AtomicReference<Point> start = new AtomicReference<>();
        final AtomicReference<Point> end = new AtomicReference<>();
        Point[][] matrix = processLines(allLines, neighbours, start, end);
        calculateNeighbours(matrix, neighbours);

        if (!getMinFromLowerElevation) {
            return getDistanceFrom(neighbours, start.get(), end.get());
        } else {
            return getDistanceFrom(
                    neighbours,
                    Arrays.stream(matrix).flatMap(Arrays::stream)
                            .filter(point -> point.elevation.equals(LOWER))
                            .collect(Collectors.toList()),
                    end.get());
        }
    }

    private Point[][] processLines(List<String> allLines, Map<Integer, List<Node>> neighbours, AtomicReference<Point> start, AtomicReference<Point> end) {
        Point[][] matrix = new Point[allLines.size()][allLines.get(0).length()];

        String line;
        char letter;
        int counter = 0;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                letter = line.charAt(j);
                switch (letter) {
                    case 'E':
                        matrix[i][j] = new Point(counter, HIGHER);
                        end.set(matrix[i][j]);
                        break;
                    case 'S':
                        matrix[i][j] = new Point(counter, LOWER);
                        start.set(matrix[i][j]);
                        break;
                    default:
                        matrix[i][j] = new Point(counter, (int) letter);
                        break;
                }
                neighbours.put(counter, new ArrayList<>());
                counter++;
            }
        }

        return matrix;
    }

    private void calculateNeighbours(Point[][] matrix, Map<Integer, List<Node>> neighbours) {
        Point me;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                me = matrix[i][j];
                if (i > 0 && correctElevation(me, matrix[i - 1][j])) {
                    // UP: i -1, j
                    neighbours.get(me.id).add(new Node(matrix[i - 1][j].id, WEIGHT));
                }
                if (i < matrix.length - 1 && correctElevation(matrix[i][j], matrix[i + 1][j])) {
                    // DOWN: i + 1, j
                    neighbours.get(me.id).add(new Node(matrix[i + 1][j].id, WEIGHT));
                }
                if (j > 0 && correctElevation(matrix[i][j], matrix[i][j - 1])) {
                    // LEFT: i, j - 1
                    neighbours.get(me.id).add(new Node(matrix[i][j - 1].id, WEIGHT));
                }
                if (j < matrix[i].length - 1 && correctElevation(matrix[i][j], matrix[i][j + 1])) {
                    // RIGHT: i, j + 1
                    neighbours.get(me.id).add(new Node(matrix[i][j + 1].id, WEIGHT));
                }
            }
        }
    }

    private int getDistanceFrom(Map<Integer, List<Node>> neighbours, Point start, Point end) {
        return getDistanceFrom(neighbours, List.of(start), end);
    }

    private int getDistanceFrom(Map<Integer, List<Node>> neighbours, List<Point> startList, Point end) {
        return new Graph(neighbours)
                .algorithm(startList.stream().map(Point::getId).collect(Collectors.toList()))
                .getDistances()[end.id];
    }

    private boolean correctElevation(Point p1, Point p2) {
        return (p2.elevation == p1.elevation + 1) || (p2.elevation <= p1.elevation);
    }

    public static void main(String[] args) {
        Day.run(Day12::new, "2022/D12_small.txt", "2022/D12_custom.txt", "2022/D12_full.txt");
    }
}
