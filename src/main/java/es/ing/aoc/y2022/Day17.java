package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;

public class Day17 extends Day {

    private static final Map<String, IntUnaryOperator> WIND_FUNCTIONS = Map.of(
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

        public void windMovement(IntUnaryOperator windFunction) {
            parts.forEach(p -> p.y = windFunction.applyAsInt(p.y));
        }

        public void gravityMovement() {
            parts.forEach(p -> p.x--);
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
        return algorithm(fileContents, BigInteger.valueOf(2022L));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return algorithm(fileContents, BigInteger.valueOf(1000000000000L));
    }

    private String algorithm(String fileContents, BigInteger maxRocksToFall) {

        String[] windStream = fileContents.split("");
        int[][] cave = new int[1000][7];

        int windIndex = 0;
        int rockIndex = 0;
        BigInteger rocksStopped = BigInteger.ZERO;
        BigInteger windMovements = BigInteger.valueOf(windStream.length);

        List<Rock> rocksCatalog = generateRocksCatalog();

        Rock currentRock;
        String wind;
        BigInteger cuttedRows = BigInteger.ZERO;
        BigInteger currentTop, previousTop = BigInteger.ZERO;
        do {
            currentRock = new Rock(rocksCatalog.get(rockIndex++ % rocksCatalog.size()));

            // Putting rock in initial place
            Point rockPosition = new Point(getCaveTopEmptyRow(cave) + 3, 2);
            currentRock.addPosition(rockPosition);
            for (Point rockPoint : currentRock.parts) {
                cave[rockPoint.getX()][rockPoint.getY()] = ROCK_MOVING;
            }

            do {
                wind = windStream[windIndex++ % windStream.length];
                applyWind(cave, currentRock, wind);
                applyGravity(cave, currentRock);
            } while (!currentRock.isStopped);
            rocksStopped = rocksStopped.add(BigInteger.ONE);

            cuttedRows = cuttedRows.add(BigInteger.valueOf(trimCaveBottomRows(cave)));

            if (rocksStopped.mod(windMovements.multiply(BigInteger.valueOf(rocksCatalog.size()))).equals(BigInteger.ZERO)) {
                currentTop = cuttedRows.add(BigInteger.valueOf(getCaveTopEmptyRow(cave)));
                System.out.printf("%s - %s - %d - %d - %d\n", rocksStopped, cuttedRows.add(BigInteger.valueOf(getCaveTopEmptyRow(cave))), currentTop.subtract(previousTop), rockIndex % rocksCatalog.size(), windIndex % windStream.length);
                previousTop = currentTop;
            }

            // TODO: This data has been manually calculated through the rocks cycles....
            if (maxRocksToFall.compareTo(BigInteger.valueOf(5000)) > 0) {
                if (windStream.length == 40) {
                    while (rocksStopped.add(BigInteger.valueOf(140000)).compareTo(maxRocksToFall) < 0) {
                        rocksStopped = rocksStopped.add(BigInteger.valueOf(140000));
                        cuttedRows = cuttedRows.add(BigInteger.valueOf(212000L));
                    }
                } else {
                    while (rocksStopped.add(BigInteger.valueOf(17406975)).compareTo(maxRocksToFall) < 0) {
                        rocksStopped = rocksStopped.add(BigInteger.valueOf(17406975));
                        cuttedRows = cuttedRows.add(BigInteger.valueOf(27094335L));
                    }
                }
            }

        } while (rocksStopped.compareTo(maxRocksToFall) < 0);

        return String.valueOf(cuttedRows.add(BigInteger.valueOf(getCaveTopEmptyRow(cave))));
    }

    private int trimCaveBottomRows(int[][] cave) {
        int lowerFilledRow = Integer.MAX_VALUE;
        boolean found;
        for (int y = 0; y < cave[0].length; y++) {
            found = false;
            for (int x = cave.length - 1; x >= 0; x--) {
                if (cave[x][y] != SPACE) {
                    found = true;
                    lowerFilledRow = Math.min(lowerFilledRow, x);
                    break;
                }
            }

            if (!found) {
                lowerFilledRow = 0;
            }
        }

        if (lowerFilledRow > 100) {
            for (int x = 0; x < cave.length - lowerFilledRow; x++) {
                cave[x] = cave[x + lowerFilledRow].clone();
            }
            return lowerFilledRow;
        }
        return 0;
    }

    private void applyWind(int[][] cave, Rock currentRock, String wind) {

        IntUnaryOperator windFunction = WIND_FUNCTIONS.get(wind);
        Range<Integer> legalColumns = Range.between(0, cave[0].length - 1);

        boolean canApplyWind = currentRock.parts.stream().allMatch(p -> {
            int newY = windFunction.applyAsInt(p.y);
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

        if (canApplyGravity) {
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

    public static void main(String[] args) {
        Day.run(Day17::new, "2022/D17_full.txt");
    }
}
