package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Day24 extends Day {

    private static final Integer EMPTY = 0;
    private static final Integer WALL = Integer.MAX_VALUE;

    public enum Direction {
        UP("^", p -> new Point(p.x - 1, p.y)),
        LEFT("<", p -> new Point(p.x, p.y - 1)),
        DOWN("v", p -> new Point(p.x + 1, p.y)),
        RIGHT(">", p -> new Point(p.x, p.y + 1));

        private final String letter;
        private final UnaryOperator<Point> fn;

        Direction(String letter, UnaryOperator<Point> fn) {
            this.letter = letter;
            this.fn = fn;
        }

        public static Direction of(String letter) {
            return Arrays.stream(Direction.values())
                    .filter(d -> d.letter.equalsIgnoreCase(letter)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Unknown direction"));
        }

        @Override
        public String toString() {
            return letter;
        }
    }

    static class Lizzard extends Point {
        private final Direction dir;

        public Lizzard(int x, int y, Direction dir) {
            super(x, y);
            this.dir = dir;
        }

        public void updatePosition(Point newPos) {
            this.x = newPos.x;
            this.y = newPos.y;
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        return lizzardsAlgorithm(fileContents, false);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return lizzardsAlgorithm(fileContents, true);
    }

    private String lizzardsAlgorithm(String fileContents, boolean tripBackAndForward) {
        List<Lizzard> lizzards = readBoardFromFile(fileContents);
        int[][] matrix = createCurrentMatrixFrom(lizzards, fileContents);

        Point start = new Point(0, 1);
        Point end = new Point(matrix.length - 1, matrix[0].length - 2);

        int minMovements = lizzardsLoop(lizzards, matrix, start, end);
        if (tripBackAndForward) {
            minMovements += lizzardsLoop(lizzards, matrix, end, start);
            minMovements += lizzardsLoop(lizzards, matrix, start, end);
        }

        return String.valueOf(minMovements);
    }

    private List<Lizzard> readBoardFromFile(String fileContents) {
        List<String> allLines = new ArrayList<>(Arrays.asList(fileContents.split(System.lineSeparator()))); // when input file is multiline

        List<Lizzard> lizzards = new ArrayList<>();
        String line;
        for (int i = 1; i < allLines.size() - 1; i++) {
            line = allLines.get(i);
            for (int j = 1; j < line.length() - 1; j++) {
                if ('.' != line.charAt(j)) {
                    lizzards.add(new Lizzard(i, j, Direction.of(String.valueOf(line.charAt(j)))));
                }
            }
        }

        return lizzards;
    }

    private int[][] createCurrentMatrixFrom(List<Lizzard> lizzards, String fileContents) {
        List<String> allLines = new ArrayList<>(Arrays.asList(fileContents.split(System.lineSeparator()))); // when input file is multiline

        int[][] matrix = new int[allLines.size()][allLines.get(0).length()];

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                if (x == 0 || y == 0 || x == matrix.length - 1 || y == matrix[0].length - 1) {
                    matrix[x][y] = WALL;
                } else {
                    matrix[x][y] = EMPTY;
                }
            }
        }
        matrix[0][1] = EMPTY;
        matrix[matrix.length - 1][matrix[0].length - 2] = EMPTY;

        for (Lizzard liz : lizzards) {
            matrix[liz.x][liz.y]++;
        }

        return matrix;
    }

    private int lizzardsLoop(List<Lizzard> lizzards, int[][] matrix, Point start, Point end) {

        final Direction[] prefs = {Direction.RIGHT, Direction.DOWN, Direction.UP, Direction.LEFT};

        Set<Point> humans = new HashSet<>(List.of(start));
        int minute = 0;
        do {
            // Lizzards movements
            for (Lizzard liz : lizzards) {
                Point newPos = liz.dir.fn.apply(liz);

                if (matrix[newPos.x][newPos.y] == WALL) {
                    switch (liz.dir) {
                        case DOWN:
                            newPos = new Point(1, newPos.y);
                            break;
                        case UP:
                            newPos = new Point(matrix.length - 2, newPos.y);
                            break;
                        case LEFT:
                            newPos = new Point(newPos.x, matrix[0].length - 2);
                            break;
                        case RIGHT:
                            newPos = new Point(newPos.x, 1);
                            break;
                    }
                }
                matrix[liz.x][liz.y]--;
                matrix[newPos.x][newPos.y]++;
                liz.updatePosition(newPos);
            }

            // Human movements
            Set<Point> nextGenHumans = new HashSet<>();
            for (Point human : humans) {
                if (matrix[human.x][human.y] == EMPTY) {
                    nextGenHumans.add(human);
                }

                for (Direction d : prefs) {
                    Point newPos = d.fn.apply(human);
                    if (positionWithinLimits(newPos, matrix) && matrix[newPos.x][newPos.y] == EMPTY) {
                        nextGenHumans.add(newPos);
                    }
                }
            }
            humans.clear();
            humans.addAll(nextGenHumans);
            minute++;

        } while (!humans.contains(end));

        return minute;
    }

    private boolean positionWithinLimits(Point newPos, int[][] matrix) {
        return newPos.x >= 0 && newPos.x <= matrix.length - 1 && newPos.y >= 0 && newPos.y <= matrix[0].length - 1;
    }

    public static void main(String[] args) {
        Day.run(Day24::new, "2022/D24_small.txt", "2022/D24_full.txt");
    }
}
