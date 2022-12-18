package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day18 extends Day {

    private static final Integer CUBE_SIZE = 1;

    private List<Pair<Point, Point>> getCubeSides(Point p1, int t) {
        final List<Pair<Point, Point>> sides = new ArrayList<>();
        final Point p2 = new Point(p1.x + t, p1.y + t, p1.z + t);
        sides.add(Pair.of(p1, new Point(p1.x, p1.y + t, p1.z + t)));
        sides.add(Pair.of(p1, new Point(p1.x + t, p1.y, p1.z + t)));
        sides.add(Pair.of(p1, new Point(p1.x + t, p1.y + t, p1.z)));
        sides.add(Pair.of(new Point(p2.x, p2.y - t, p2.z - t), p2));
        sides.add(Pair.of(new Point(p2.x - t, p2.y, p2.z - t), p2));
        sides.add(Pair.of(new Point(p2.x - t, p2.y - t, p2.z), p2));
        return sides;
    }

    @Override
    protected String part1(String fileContents) throws Exception {
        return String.valueOf(algorithm(fileContents, false));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        return String.valueOf(algorithm(fileContents, true));
    }

    private int algorithm(String fileContents, boolean filterSidesTouchingLava) {
        String[] coords = fileContents.split(System.lineSeparator()); // when input file is multiline

        List<Point> allCubes = new ArrayList<>();
        for (String c : coords) {
            if (c.length() > 0) {
                String[] parts = c.split(",");
                allCubes.add(new Point(parts[0], parts[1], parts[2]));
            }
        }

        Map<Pair<Point, Point>, Integer> sideCounter = new HashMap<>();

        for (Point cube : allCubes) {
            getCubeSides(cube, CUBE_SIZE).forEach(side -> {
                if (sideCounter.containsKey(side)) {
                    sideCounter.put(side, sideCounter.get(side) + 1);
                } else {
                    sideCounter.put(side, 1);
                }
            });
        }

        List<Pair<Point, Point>> nonRepeatedSides = sideCounter.entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (filterSidesTouchingLava) {
            return getNonTrappedSides(nonRepeatedSides, allCubes).size();
        } else {
            return nonRepeatedSides.size();
        }
    }

    private List<Pair<Point, Point>> getNonTrappedSides(List<Pair<Point, Point>> sides, List<Point> cubes) {

        List<Point> superCube = this.getSuperCube(cubes);
        List<Point> cubesWithLava = getCubesWithLava(superCube.get(0), new ArrayList<>(), superCube, cubes);

        List<Pair<Point, Point>> lavaSides = cubesWithLava.stream()
                .flatMap(c -> getCubeSides(c, CUBE_SIZE).stream())
                .collect(Collectors.toList());

        return sides.stream().filter(lavaSides::contains).collect(Collectors.toList());
    }

    private List<Point> getCubesWithLava(Point current, List<Point> cubesWithLava, List<Point> superCube, List<Point> presentCubes) {
        cubesWithLava.add(current);
        for (Point neighbour : getCubeNeighbours(current)) {
            if (!cubesWithLava.contains(neighbour) && superCube.contains(neighbour) && !presentCubes.contains(neighbour)) {
                getCubesWithLava(neighbour, cubesWithLava, superCube, presentCubes);
            }
        }
        return cubesWithLava;
    }

    private List<Point> getSuperCube(List<Point> cubes) {
        List<Point> allMatrixCubes = new ArrayList<>();
        List<Integer> xCoords = cubes.stream().map(Point::getX).sorted().collect(Collectors.toList());
        Pair<Integer, Integer> xLimits = Pair.of(xCoords.get(0), xCoords.get(xCoords.size() - 1));

        List<Integer> yCoords = cubes.stream().map(Point::getY).sorted().collect(Collectors.toList());
        Pair<Integer, Integer> yLimits = Pair.of(yCoords.get(0), yCoords.get(yCoords.size() - 1));

        List<Integer> zCoords = cubes.stream().map(Point::getZ).sorted().collect(Collectors.toList());
        Pair<Integer, Integer> zLimits = Pair.of(zCoords.get(0), zCoords.get(zCoords.size() - 1));

        for (int x = xLimits.a - 1; x <= xLimits.b + 1; x++) {
            for (int y = yLimits.a - 1; y <= yLimits.b + 1; y++) {
                for (int z = zLimits.a - 1; z <= zLimits.b + 1; z++) {
                    allMatrixCubes.add(new Point(x, y, z));
                }
            }
        }

        return allMatrixCubes;
    }

    private List<Point> getCubeNeighbours(Point p) {
        List<Point> neighbours = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) + Math.abs(y) + Math.abs(z) == 1) {
                        neighbours.add(new Point(p.x + x, p.y + y, p.z + z));
                    }
                }
            }
        }
        return neighbours;
    }

    public static void main(String[] args) {
        Day.run(Day18::new, "2022/D18_small.txt", "2022/D18_full.txt");
    }
}
