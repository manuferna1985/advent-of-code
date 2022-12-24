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

        List<Lizzard> lizzards = readBoardFromFile(fileContents);
        int[][] matrix = createCurrentMatrixFrom(lizzards, fileContents);

        int minMovements = lizzardsLoop(
                lizzards,
                matrix,
                new Point(0, 1),
                new Point(matrix.length - 1, matrix[0].length - 2));

        return String.valueOf(minMovements);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        return String.valueOf(2);
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
                    matrix[x][y] = Integer.MAX_VALUE;
                }
            }
        }
        matrix[0][1] = 0;
        matrix[matrix.length - 1][matrix[0].length - 2] = 0;

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
            System.out.printf("%-5d - %d\n", minute, humans.size());

            // Lizzards movements
            for (Lizzard liz : lizzards) {
                Point newPos = liz.dir.fn.apply(liz);

                if (matrix[newPos.x][newPos.y] == Integer.MAX_VALUE) {
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
                if (matrix[human.x][human.y] == 0) {
                    System.out.printf("\t%s --> %s\n", human, human);
                    nextGenHumans.add(human);
                }

                for (Direction d : prefs) {
                    Point newPos = d.fn.apply(human);
                    if (newPos.x >= 0 && newPos.y >= 0 && matrix[newPos.x][newPos.y] == 0) {
                        System.out.printf("\t%s --> %s\n", human, newPos);
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

    public static void main(String[] args) {
        Day.run(Day24::new, "2022/D24_small.txt", "2022/D24_full.txt");
    }
}
