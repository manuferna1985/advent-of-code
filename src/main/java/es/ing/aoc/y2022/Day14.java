package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 extends Day {

    private static final Integer SPACE = 0;
    private static final Integer ROCK = 1;
    private static final Integer SAND = 2;
    private static final Point INITIAL_SAND = new Point(0, 500);
    private static final boolean DEBUG = false;

    @Override
    protected String part1(String fileContents) throws Exception {
        return algorithm(fileContents, false);
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return algorithm(fileContents, true);
    }

    private String algorithm(String fileContents, boolean addFloor) {
        List<List<Point>> rockPaths = readRockPaths(fileContents);
        int[][] cave = createCaveFrom(rockPaths, addFloor);

        for (List<Point> path : rockPaths) {
            for (int i = 0; i < path.size() - 1; i++) {
                putRockLineWith(cave, path.get(i), path.get(i + 1));
            }
        }

        boolean rest;
        int unitsToRest = 0;
        do {
            Point movingSand = new Point(INITIAL_SAND);
            rest = addSandUnit(cave, movingSand);
            if (rest) {
                unitsToRest++;
            }
            if (!rest || movingSand.equals(INITIAL_SAND)) {
                printCave(cave, movingSand.y - 200);
                return String.valueOf(unitsToRest);
            }
        } while (true);
    }

    private List<List<Point>> readRockPaths(String fileContents) {
        String[] paths = fileContents.split(System.lineSeparator()); // when input file is multiline
        List<List<Point>> rockPaths = new ArrayList<>();
        for (String line : paths) {
            String[] parts = line.split(" -> ");

            List<Point> points = new ArrayList<>();
            for (String p : parts) {
                String[] coords = p.split(",");
                points.add(new Point(Integer.parseInt(coords[1]), Integer.parseInt(coords[0])));
            }

            rockPaths.add(points);
        }
        return rockPaths;
    }

    private int[][] createCaveFrom(List<List<Point>> rockPaths, boolean addFloor) {
        List<Point> allPoints = rockPaths.stream().flatMap(Collection::stream).collect(Collectors.toList());
        int maxX = allPoints.stream().mapToInt(Point::getX).max().orElse(0);
        int maxY = allPoints.stream().mapToInt(Point::getY).max().orElse(0);

        if (addFloor) {
            rockPaths.add(List.of(new Point(maxX + 2, 0), new Point(maxX + 2, maxY * 2)));
            maxX += 2;
            maxY *= 2;
        }

        return new int[maxX + 1][maxY + 1];
    }

    private boolean addSandUnit(int[][] cave, Point sand) {
        boolean rest = false;
        boolean infiniteFall = false;

        while (!rest && !infiniteFall) {
            if (cave[sand.x + 1][sand.y - 1] != SPACE && cave[sand.x + 1][sand.y] != SPACE && cave[sand.x + 1][sand.y + 1] != SPACE) {
                rest = true;
                cave[sand.x][sand.y] = SAND;
            } else {
                if (cave[sand.x + 1][sand.y] == SPACE) {
                    // down
                    sand.x++;
                } else if (cave[sand.x + 1][sand.y - 1] == SPACE) {
                    // diagonal left
                    sand.x++;
                    sand.y--;
                } else {
                    // diagonal right
                    sand.x++;
                    sand.y++;
                }
            }
            if (sand.x >= cave.length - 1 || sand.y >= cave[0].length - 1) {
                infiniteFall = true;
            }
        }
        return rest;
    }

    private void putRockLineWith(int[][] cave, Point a, Point b) {
        for (Integer x : IntStream.rangeClosed(Math.min(a.x, b.x), Math.max(a.x, b.x)).toArray()) {
            for (Integer y : IntStream.rangeClosed(Math.min(a.y, b.y), Math.max(a.y, b.y)).toArray()) {
                cave[x][y] = ROCK;
            }
        }
    }

    private void printCave(int[][] cave, int minY) {
        if (DEBUG) {
            System.out.println("-------------------------------------------------------------");
            for (int x = 0; x < cave.length; x++) {
                System.out.printf("%-5d: ", x);
                for (int y = minY; y < cave[x].length; y++) {
                    System.out.print(cave[x][y]);
                }
                System.out.println();
            }
            System.out.println("-------------------------------------------------------------");
        }
    }

    public static void main(String[] args) {
        Day.run(Day14::new, "2022/D14_small.txt", "2022/D14_full.txt");
    }
}
