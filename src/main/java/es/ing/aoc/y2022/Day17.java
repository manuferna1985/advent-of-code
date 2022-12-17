package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;

public class Day17 extends Day {

    private static final Integer TOTAL_ROCKS = 2022;

    private static final Map<String, UnaryOperator<Integer>> WIND_FUNCTIONS = Map.of(
            "<", y -> y - 1,
            ">", y -> y + 1);

    private static final Integer SPACE = 0;
    private static final Integer ROCK_MOVING = 1;
    private static final Integer ROCK_STOPPED = 2;

    static class Rock {
        List<Point> parts;
        boolean isStopped;

        Rock(List<Point> parts) {
            this.parts = parts;
            this.isStopped = false;
        }

        Rock(Rock other) {
            this(other.parts.stream().map(Point::new).collect(Collectors.toList()));
        }

        public void addPosition(Point pos) {
            parts.forEach(p -> {
                p.x = p.x + pos.x;
                p.y = p.y + pos.y;
            });
        }

        public void windMovement(UnaryOperator<Integer> windFunction) {
            parts.forEach(p -> {
                p.y = windFunction.apply(p.y);
            });
        }

        public void gravityMovement() {
            parts.forEach(p -> {
                p.x--;
            });
        }

        public void stop() {
            this.isStopped = true;
        }

        public boolean isStopped() {
            return this.isStopped;
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {

        String[] windStream = fileContents.split("");
        int[][] cave = new int[50000][7];
        int maxXToPrint = 10;

        int windIndex = 0;
        int rockIndex = 0;
        int rocksStopped = 0;

        List<Rock> rocksCatalog = generateRocksCatalog();

        Rock currentRock;
        String wind;
        do {
            currentRock = new Rock(rocksCatalog.get(rockIndex++ % rocksCatalog.size()));

            //printCave(cave, maxXToPrint);

            // Putting rock in initial place
            Point rockPosition = new Point(getCaveTopEmptyRow(cave) + 3, 2);
            currentRock.addPosition(rockPosition);
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = ROCK_MOVING;
            }

            //printCave(cave, maxXToPrint);

            do {
                wind = windStream[windIndex++ % windStream.length];

                applyWind(cave, currentRock, wind);
                //printCave(cave, maxXToPrint);

                applyGravity(cave, currentRock);
                //printCave(cave, maxXToPrint);

            } while (!currentRock.isStopped);
            rocksStopped++;

        } while (rocksStopped < TOTAL_ROCKS);

        return String.valueOf(getCaveTopEmptyRow(cave));
    }

    private void applyWind(int[][] cave, Rock currentRock, String wind) {

        UnaryOperator<Integer> windFunction = WIND_FUNCTIONS.get(wind);
        Range<Integer> legalColumns = Range.between(0, cave[0].length - 1);

        boolean canApplyWind = currentRock.parts.stream().allMatch(p -> {
            int newY = windFunction.apply(p.y);
            return legalColumns.contains(newY) && cave[p.x][newY] != ROCK_STOPPED;
        });

        if (canApplyWind) {
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = SPACE;
            }
            currentRock.windMovement(windFunction);
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = ROCK_MOVING;
            }
        }
    }

    private void applyGravity(int[][] cave, Rock currentRock) {

        boolean canApplyGravity = currentRock.parts.stream()
                .allMatch(p -> p.x > 0 && cave[p.x - 1][p.y] != ROCK_STOPPED);

        if (canApplyGravity){
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = SPACE;
            }
            currentRock.gravityMovement();
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = ROCK_MOVING;
            }
        } else {
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = ROCK_STOPPED;
            }
            currentRock.stop();
        }
    }

    private int getCaveTopEmptyRow(int[][] cave) {
        boolean something;
        int firstEmptyRow = -1;
        for (int x = 0; x < cave.length; x++) {
            something = false;
            for (int y = 0; y < cave[x].length; y++) {
                if (cave[x][y] != 0) {
                    something = true;
                    break;
                }
            }
            if (!something) {
                firstEmptyRow = x;
                break;
            }
        }

        return firstEmptyRow;
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline

        return String.valueOf(2);
    }

    private List<Rock> generateRocksCatalog() {
        List<Rock> rocksCatalog = new ArrayList<>();
        // Horizontal
        rocksCatalog.add(new Rock(List.of(Point.of(0, 0), Point.of(0, 1), Point.of(0, 2), Point.of(0, 3))));
        // Star
        rocksCatalog.add(new Rock(List.of(Point.of(0, 1), Point.of(1, 0), Point.of(1, 1), Point.of(1, 2), Point.of(2, 1))));
        // L
        rocksCatalog.add(new Rock(List.of(Point.of(0, 0), Point.of(0, 1), Point.of(0, 2), Point.of(1, 2), Point.of(2, 2))));
        // Vertical
        rocksCatalog.add(new Rock(List.of(Point.of(0, 0), Point.of(1, 0), Point.of(2, 0), Point.of(3, 0))));
        // Square
        rocksCatalog.add(new Rock(List.of(Point.of(0, 0), Point.of(0, 1), Point.of(1, 0), Point.of(1, 1))));
        return rocksCatalog;
    }

    private void printCave(int[][] cave, int maxX) {
        System.out.println("--------------------------------------");
        for (int x = maxX; x >= 0; x--) {
            System.out.printf("%-5d - ", x);
            for (int y = 0; y < cave[x].length; y++) {
                System.out.printf(" %d ", cave[x][y]);
            }
            System.out.println();
        }
        System.out.println("--------------------------------------");
    }

    public static void main(String[] args) {
        Day.run(Day17::new, "2022/D17_small.txt", "2022/D17_full.txt");
    }
}
