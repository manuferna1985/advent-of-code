package es.ing.aoc.y2022;

import static es.ing.aoc.y2022.Day22.Tile.FLOOR;
import static es.ing.aoc.y2022.Day22.Tile.VOID;
import static es.ing.aoc.y2022.Day22.Tile.WALL;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.MatrixUtils;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        RIGHT,
        LEFT,
        REVERSE,
        NONE;
    }

    static class Cell {
        Tile tile;
        Point originPosition;

        public Cell(Tile tile, Point originPosition) {
            this.tile = tile;
            this.originPosition = originPosition;
        }

        public static Cell of(Tile tile, Point originPosition) {
            return new Cell(tile, originPosition);
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        Cell[][] board = readBoardFromFile(fileContents);
        List<String> instructions = getInstructions(fileContents);
        Pair<Point, Direction> start = Pair.of(getStartingPoint(board), Direction.RIGHT);
        Pair<Point, Direction> end = followPath(board, instructions, start);
        return String.valueOf(getPositionPoints(end));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        Cell[][] board = readBoardFromFile(fileContents);
        transformBoardToStandardCube(board, inferSideSizeFromBoard(board));
        return String.valueOf(2);
    }

    private int getPositionPoints(Pair<Point, Direction> position) {
        return ((position.a.x + 1) * 1000) + ((position.a.y + 1) * 4) + position.b.points;
    }

    private int inferSideSizeFromBoard(Cell[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        // Input cube (for good shaped ones) will be always 3x4 or 4x3, so:
        return Math.max(rows, cols) - Math.min(rows, cols);
    }

    private Pair<Point, Direction> followPath(Cell[][] board, List<String> instructions, Pair<Point, Direction> start) {

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
                    while (VOID.equals(board[newPos.a.x][newPos.a.y].tile)) {
                        newPos = moveToSafePosition(newPos, board);
                    }
                    if (FLOOR.equals(board[newPos.a.x][newPos.a.y].tile)) {
                        current = newPos;
                    } else if (WALL.equals(board[newPos.a.x][newPos.a.y].tile)) {
                        break;
                    }
                }
            }
        }
        return current;
    }

    private Pair<Point, Direction> moveToSafePosition(Pair<Point, Direction> current, Cell[][] board) {
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

    private Cell[][] readBoardFromFile(String fileContents) {
        List<String> allLines = new ArrayList<>(Arrays.asList(fileContents.split(System.lineSeparator()))); // when input file is multiline

        allLines.remove(allLines.size() - 1);
        allLines.remove(allLines.size() - 1);

        int maxColumns = allLines.stream().mapToInt(String::length).max().orElse(0);
        Cell[][] board = new Cell[allLines.size()][maxColumns];

        String line;
        for (int i = 0; i < allLines.size(); i++) {
            line = allLines.get(i);
            for (int j = 0; j < maxColumns; j++) {
                if (j < line.length()) {
                    board[i][j] = Cell.of(Tile.of(String.valueOf(line.charAt(j))), Point.of(i, j));
                } else {
                    board[i][j] = Cell.of(Tile.VOID, Point.of(i, j));
                }
            }
        }
        return board;
    }

    private void printBoard(Cell[][] board) {
        System.out.println("-------------------------------------------");

        for (Cell[] tiles : board) {
            for (Cell tile : tiles) {
                System.out.print(tile);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");
    }

    private void transformBoardToStandardCube(Cell[][] board, int dim) {

        boolean shapedCube = false;
        do {
            List<Point> sides = calculateActiveSidesFromBoard(board, dim);
            System.out.println("Current sides: " + sides);
            printReadableSides(sides);

            int baseCoord = 1;
            int horizontalCubes = (int) sides.stream().filter(p -> p.x == baseCoord).count();
            int verticalCubes = (int) sides.stream().filter(p -> p.y == baseCoord).count();

            if (horizontalCubes == 4) {

                Point topCube = sides.stream().filter(p -> p.x == baseCoord - 1).findFirst().orElseThrow(() -> new RuntimeException("Wrong cube!"));
                Point downCube = sides.stream().filter(p -> p.x == baseCoord + 1).findFirst().orElseThrow(() -> new RuntimeException("Wrong cube!"));

                if (topCube.y > baseCoord) {
                    rotateAndMoveSide(board, topCube, dim, Turn.LEFT, Direction.LEFT);
                } else if (topCube.y < baseCoord) {
                    rotateAndMoveSide(board, topCube, dim, Turn.RIGHT, Direction.RIGHT);
                } else {
                    System.out.println("Top cube is OK!");
                }
                if (downCube.y > baseCoord) {
                    rotateAndMoveSide(board, downCube, dim, Turn.RIGHT, Direction.LEFT);
                } else if (downCube.y < baseCoord) {
                    rotateAndMoveSide(board, downCube, dim, Turn.LEFT, Direction.RIGHT);
                } else {
                    System.out.println("Down cube is OK!");
                }
                shapedCube = topCube.y == baseCoord && downCube.y == baseCoord;

            } else if (verticalCubes == 4) {

                Point leftCube = sides.stream().filter(p -> p.y == baseCoord - 1).findFirst().orElseThrow(() -> new RuntimeException("Wrong cube!"));
                Point rightCube = sides.stream().filter(p -> p.y == baseCoord + 1).findFirst().orElseThrow(() -> new RuntimeException("Wrong cube!"));

                if (leftCube.x > baseCoord) {
                    rotateAndMoveSide(board, leftCube, dim, Turn.RIGHT, Direction.UP);
                } else if (leftCube.x < baseCoord) {
                    rotateAndMoveSide(board, leftCube, dim, Turn.LEFT, Direction.DOWN);
                } else {
                    System.out.println("Left cube is OK!");
                }
                if (rightCube.x > baseCoord) {
                    rotateAndMoveSide(board, rightCube, dim, Turn.LEFT, Direction.UP);
                } else if (rightCube.x < baseCoord) {
                    rotateAndMoveSide(board, rightCube, dim, Turn.RIGHT, Direction.DOWN);
                } else {
                    System.out.println("Right cube is OK!");
                }
                shapedCube = leftCube.x == baseCoord && rightCube.x == baseCoord;

            } else if (horizontalCubes == 3) {
                int leftCol = sides.stream().filter(p -> p.x == baseCoord).mapToInt(p -> p.y).min().orElse(-1);
                int rightCol = sides.stream().filter(p -> p.x == baseCoord).mapToInt(p -> p.y).max().orElse(-1);

                List<Point> cornerSpots = List.of(
                        Point.of(baseCoord - 1, leftCol - 1),
                        Point.of(baseCoord + 1, leftCol - 1),
                        Point.of(baseCoord - 1, rightCol + 1),
                        Point.of(baseCoord + 1, rightCol + 1));

                Point firstCorner = null;
                for (Point corner : cornerSpots) {
                    if (sides.contains(corner)) {
                        firstCorner = corner;
                        break;
                    }
                }
                if (firstCorner == null) {
                    throw new RuntimeException("No first corner found in sides array!");
                }
                System.out.println("First corner(relative): " + firstCorner);

                if (firstCorner.x > baseCoord && firstCorner.y > rightCol) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.LEFT, Direction.UP);
                } else if (firstCorner.x > baseCoord && firstCorner.y < leftCol) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.RIGHT, Direction.UP);
                } else if (firstCorner.x < baseCoord && firstCorner.y > rightCol) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.RIGHT, Direction.DOWN);
                } else {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.LEFT, Direction.DOWN);
                }
            } else if (verticalCubes == 3) {
                int topRow = sides.stream().filter(p -> p.y == baseCoord).mapToInt(p -> p.x).min().orElse(-1);
                int downRow = sides.stream().filter(p -> p.y == baseCoord).mapToInt(p -> p.x).max().orElse(-1);

                List<Point> cornerSpots = List.of(
                        Point.of(topRow - 1, baseCoord - 1),
                        Point.of(topRow - 1, baseCoord + 1),
                        Point.of(downRow + 1, baseCoord - 1),
                        Point.of(downRow + 1, baseCoord + 1));

                Point firstCorner = null;
                for (Point corner : cornerSpots) {
                    if (sides.contains(corner)) {
                        firstCorner = corner;
                        break;
                    }
                }
                if (firstCorner == null) {
                    throw new RuntimeException("No first corner found in sides array!");
                }
                System.out.println("First corner(relative): " + firstCorner);

                if (firstCorner.y > baseCoord && firstCorner.x > downRow) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.RIGHT, Direction.LEFT);
                } else if (firstCorner.y > baseCoord && firstCorner.x < topRow) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.LEFT, Direction.LEFT);
                } else if (firstCorner.y < baseCoord && firstCorner.x > downRow) {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.LEFT, Direction.RIGHT);
                } else {
                    rotateAndMoveSide(board, firstCorner, dim, Turn.RIGHT, Direction.RIGHT);
                }

            } else {
                throw new RuntimeException("No base condition detected. Is input cube a correct shaped cube?");
            }

        } while (!shapedCube);

/*
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

 */
    }

    private void rotateAndMoveSide(Cell[][] board, Point side, int dim, Turn turn, Direction movement) {
        System.out.printf(" >>>>> Rotate & Move command: %s - %s - %s\n", side, turn, movement);
        Point realCoords = new Point(side.x * dim, side.y * dim);

        switch (turn) {
            case LEFT:
                MatrixUtils.rotateLeft(Cell.class, board, realCoords, dim);
                break;
            case RIGHT:
                MatrixUtils.rotateRight(Cell.class, board, realCoords, dim);
                break;
            case REVERSE:
                MatrixUtils.rotateLeft(Cell.class, board, realCoords, dim);
                MatrixUtils.rotateLeft(Cell.class, board, realCoords, dim);
                break;
            case NONE:
                System.out.println("No rotation needed!");
        }

        switch (movement) {
            case UP:
                MatrixUtils.moveSubMatrix(board, realCoords, new Point(realCoords.x - dim, realCoords.y), dim, createNewVoid());
                break;
            case DOWN:
                MatrixUtils.moveSubMatrix(board, realCoords, new Point(realCoords.x + dim, realCoords.y), dim, createNewVoid());
                break;
            case LEFT:
                MatrixUtils.moveSubMatrix(board, realCoords, new Point(realCoords.x, realCoords.y - dim), dim, createNewVoid());
                break;
            case RIGHT:
                MatrixUtils.moveSubMatrix(board, realCoords, new Point(realCoords.x, realCoords.y + dim), dim, createNewVoid());
                break;
        }
    }

    private List<Point> calculateActiveSidesFromBoard(Cell[][] board, int dim) {
        List<Point> sides = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!VOID.equals(board[i][j].tile) && i % dim == 0 && j % dim == 0) {
                    sides.add(new Point(i / dim, j / dim));
                }
            }
        }
        return sides;
    }

    private Cell createNewVoid() {
        return Cell.of(VOID, new Point(-1, -1));
    }

    private void printReadableSides(List<Point> sides) {
        int n = 1;
        System.out.printf("    %d   %d   %d   %d\n", 0, 1, 2, 3);
        System.out.print("  o---o---o---o---o\n");
        for (int x = 0; x < 4; x++) {
            System.out.printf("%d |", x);
            for (int y = 0; y < 4; y++) {
                if (sides.contains(new Point(x, y))) {
                    System.out.printf(" %d |", n++);
                } else {
                    System.out.print("   |");
                }
            }
            System.out.print("\n  o---o---o---o---o\n");
        }
    }

    private Point getStartingPoint(Cell[][] board) {
        for (int j = 0; j < board[0].length; j++) {
            if (FLOOR.equals(board[0][j].tile)) {
                return new Point(0, j);
            }
        }

        throw new RuntimeException("Top row has no open tiles");
    }

    public static void main(String[] args) {
        Day.run(Day22::new, "2022/D22_small.txt", "2022/D22_full.txt");
    }
}
