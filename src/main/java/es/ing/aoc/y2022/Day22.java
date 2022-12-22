package es.ing.aoc.y2022;

import static es.ing.aoc.y2022.Day22.Direction.RIGHT;
import static es.ing.aoc.y2022.Day22.Tile.FLOOR;
import static es.ing.aoc.y2022.Day22.Tile.VOID;
import static es.ing.aoc.y2022.Day22.Tile.WALL;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22 extends Day {

    private static final String TURN_RIGHT = "R";
    private static final String TURN_LEFT = "L";

    public enum Tile {
        VOID(" "),
        FLOOR("."),
        WALL("#");

        private final String letter;

        Tile(String letter) {
            this.letter = letter;
        }

        public static Tile of(String letter) {
            return Arrays.stream(Tile.values())
                    .filter(d -> d.letter.equalsIgnoreCase(letter)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Unknown cell type"));
        }

        @Override
        public String toString() {
            return letter;
        }
    }

    public enum Direction {
        UP(3, p -> new Point(p.x - 1, p.y)),
        LEFT(2, p -> new Point(p.x, p.y - 1)),
        DOWN(1, p -> new Point(p.x + 1, p.y)),
        RIGHT(0, p -> new Point(p.x, p.y + 1));

        private final int points;
        private final UnaryOperator<Point> fn;

        Direction(int points, UnaryOperator<Point> fn) {
            this.points = points;
            this.fn = fn;
        }

        public static Direction of(int points) {
            return Arrays.stream(Direction.values())
                    .filter(d -> d.points == points).findFirst()
                    .orElseThrow(() -> new RuntimeException("Unknown direction type"));
        }

        public Direction turnRight() {
            return Direction.of((points + 1 + 4) % 4);
        }

        public Direction turnLeft() {
            return Direction.of((points - 1 + 4) % 4);
        }
    }

    public enum Turn {
        RIGHT(Direction::turnRight),
        LEFT(Direction::turnLeft),
        REVERSE(Direction::turnRight, Direction::turnRight),
        NONE();

        private final UnaryOperator<Direction>[] dirs;

        Turn(UnaryOperator<Direction>... dirs) {
            this.dirs = dirs;
        }

        public Direction getNewDirection(Direction current) {
            Direction next = current;
            for (UnaryOperator<Direction> op : dirs) {
                next = op.apply(next);
            }
            return next;
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        Tile[][] board = readBoardFromFile(fileContents);
        List<String> instructions = getInstructions(fileContents);
        Pair<Point, Direction> start = Pair.of(getStartingPoint(board), RIGHT);
        Pair<Point, Direction> end = followPath(board, instructions, start);
        return String.valueOf(((end.a.x + 1) * 1000) + ((end.a.y + 1) * 4) + end.b.points);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        Tile[][] board = readBoardFromFile(fileContents);
        createCubeFromBoard(board, 4);
        return String.valueOf(2);
    }

    private Pair<Point, Direction> followPath(Tile[][] board, List<String> instructions, Pair<Point, Direction> start) {

        Pair<Point, Direction> current = start;

        for (String order : instructions) {
            if (TURN_RIGHT.equals(order)) {
                current.b = current.b.turnRight();
            } else if (TURN_LEFT.equals(order)) {
                current.b = current.b.turnLeft();
            } else {
                int pathLength = Integer.parseInt(order);

                for (int i = 0; i < pathLength; i++) {
                    Pair<Point, Direction> newPos = moveToSafePosition(current, board);
                    while (VOID.equals(board[newPos.a.x][newPos.a.y])) {
                        newPos = moveToSafePosition(newPos, board);
                    }
                    if (FLOOR.equals(board[newPos.a.x][newPos.a.y])) {
                        current = newPos;
                    } else if (WALL.equals(board[newPos.a.x][newPos.a.y])) {
                        break;
                    }
                }
            }
        }
        return current;
    }

    private Pair<Point, Direction> moveToSafePosition(Pair<Point, Direction> current, Tile[][] board) {
        Point newPos = current.b.fn.apply(current.a);
        return Pair.of(new Point((newPos.x + board.length) % board.length, (newPos.y + board[0].length) % board[0].length), current.b);
    }

    private List<String> getInstructions(String fileContents) {
        String[] allLines = fileContents.split(System.lineSeparator()); // when input file is multiline
        Pattern instructionsPattern = Pattern.compile("(\\d+|[L-R])");
        Matcher matcher = instructionsPattern.matcher(allLines[allLines.length - 1]);

        List<String> steps = new ArrayList<>();
        while (matcher.find()) {
            steps.add(matcher.group());
        }
        return steps;
    }

    private Tile[][] readBoardFromFile(String fileContents) {
        List<String> allLines = new ArrayList<>(Arrays.asList(fileContents.split(System.lineSeparator()))); // when input file is multiline

        allLines.remove(allLines.size() - 1);
        allLines.remove(allLines.size() - 1);

        Tile[][] board = new Tile[allLines.size()][allLines.stream().mapToInt(String::length).max().orElse(0)];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            Arrays.fill(board[i], VOID);
            line = allLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                board[i][j] = Tile.of(String.valueOf(line.charAt(j)));
            }
        }
        return board;
    }

    private void printBoard(Tile[][] board) {
        System.out.println("-------------------------------------------");

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
    }

    private void createCubeFromBoard(Tile[][] board, int dim) {
        List<Point> sides = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!VOID.equals(board[i][j]) && i % dim == 0 && j % dim == 0) {
                    sides.add(new Point(i / dim, j / dim));
                }
            }
        }

        System.out.println(sides);


        int verticalCubes = (int) sides.stream().filter(p -> p.y == 1).count();
        int horizontalCubes = (int) sides.stream().filter(p -> p.x == 1).count();







        Map<Point, List<Pair<Direction, Point>>> sideNeighbours = new HashMap<>();
        sides.forEach(s -> sideNeighbours.put(s, new ArrayList<>()));

        Point p;
        for (Point side : sides) {
            // Natural neighbours
            for (Direction dir : Direction.values()) {
                p = dir.fn.apply(side);
                if (sides.contains(p)) {
                    sideNeighbours.get(side).add(Pair.of(dir, p));
                }
            }
        }

        System.out.println(sideNeighbours);
    }

    private Point getStartingPoint(Tile[][] board) {
        for (int j = 0; j < board[0].length; j++) {
            if (FLOOR.equals(board[0][j])) {
                return new Point(0, j);
            }
        }

        throw new RuntimeException("Top row has no open tiles");
    }

    public static void main(String[] args) {
        Day.run(Day22::new, "2022/D22_small.txt");//, "2022/D22_full.txt");
    }
}
