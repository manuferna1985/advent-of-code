package es.ing.aoc.y2022;

import static es.ing.aoc.common.MathUtils.getStats;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class Day23 extends Day {

    private static final Pair<IntUnaryOperator, IntUnaryOperator> N = Pair.of(x -> x - 1, y -> y);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> S = Pair.of(x -> x + 1, y -> y);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> E = Pair.of(x -> x, y -> y + 1);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> W = Pair.of(x -> x, y -> y - 1);

    private static final Pair<IntUnaryOperator, IntUnaryOperator> NE = Pair.of(x -> x - 1, y -> y + 1);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> NW = Pair.of(x -> x - 1, y -> y - 1);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> SE = Pair.of(x -> x + 1, y -> y + 1);
    private static final Pair<IntUnaryOperator, IntUnaryOperator> SW = Pair.of(x -> x + 1, y -> y - 1);

    @Override
    protected String part1(String fileContents) throws Exception {
        Set<Point> elves = readMap(fileContents);
        moveElvesAlgorithm(elves, 10);
        return String.valueOf(countEmptyGroundTiles(elves));
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        Set<Point> elves = readMap(fileContents);
        int lastIteration = moveElvesAlgorithm(elves, Integer.MAX_VALUE);
        return String.valueOf(lastIteration);
    }

    private int moveElvesAlgorithm(Set<Point> elves, int iterations) {
        printElves(elves);

        List<Pair<List<Pair<IntUnaryOperator, IntUnaryOperator>>, Pair<IntUnaryOperator, IntUnaryOperator>>> proposals = List.of(
                Pair.of(List.of(N, NE, NW), N),
                Pair.of(List.of(S, SE, SW), S),
                Pair.of(List.of(W, NW, SW), W),
                Pair.of(List.of(E, NE, SE), E));

        int proposalsOffset = 0;

        List<Pair<Point, Point>> proposedMovements = new ArrayList<>();
        final AtomicReference<Pair<List<Pair<IntUnaryOperator, IntUnaryOperator>>, Pair<IntUnaryOperator, IntUnaryOperator>>> prop = new AtomicReference<>();

        for (int i = 0; i < iterations; i++) {

            for (Point elf : elves) {
                Set<Point> neighbours = getElfNeighbours(elf);

                if (elves.parallelStream().anyMatch(neighbours::contains)) {
                    // The elf is not alone, so he has to propose a movement
                    for (int p = 0; p < proposals.size(); p++) {
                        prop.set(proposals.get((p + proposalsOffset) % proposals.size()));

                        if (elves.parallelStream().noneMatch(e -> prop.get().a.stream().map(op -> new Point(op.a.applyAsInt(elf.x), op.b.applyAsInt(elf.y))).collect(Collectors.toList()).contains(e))) {
                            Point newPoint = new Point(prop.get().b.a.applyAsInt(elf.x), prop.get().b.b.applyAsInt(elf.y));
                            proposedMovements.add(Pair.of(elf, newPoint));
                            break;
                        }
                    }
                }
            }
            System.out.printf("%-5d - %-5d\n", i, proposedMovements.size());
            if (proposedMovements.isEmpty()) {
                return i + 1;
            }

            for (int k = 0; k < proposedMovements.size(); k++) {
                Pair<Point, Point> mov = proposedMovements.get(k);
                if (proposedMovements.parallelStream().filter(mov2 -> mov2.b.equals(mov.b)).count() == 1) {
                    elves.remove(mov.a);
                    mov.a.x = mov.b.x;
                    mov.a.y = mov.b.y;
                    elves.add(mov.a);
                }
            }

            proposalsOffset++;
            proposedMovements.clear();
        }

        return iterations;
    }

    private void printElves(Set<Point> elves) {
        IntSummaryStatistics xStats = getStats(elves, Point::getX);
        IntSummaryStatistics yStats = getStats(elves, Point::getY);

        System.out.println("----------------------------------------------------");
        System.out.printf("     :  %d   ...   %d\n", yStats.getMin() - 1, yStats.getMax() + 1);
        for (int x = xStats.getMin() - 1; x <= xStats.getMax() + 1; x++) {
            System.out.printf("%-5d:  ", x);
            for (int y = yStats.getMin() - 1; y <= yStats.getMax() + 1; y++) {
                if (elves.contains(Point.of(x, y))) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
        System.out.println("----------------------------------------------------");
    }

    private int countEmptyGroundTiles(Set<Point> elves) {
        IntSummaryStatistics xStats = getStats(elves, Point::getX);
        IntSummaryStatistics yStats = getStats(elves, Point::getY);

        int emptyTiles = 0;
        for (int x = xStats.getMin(); x <= xStats.getMax(); x++) {
            for (int y = yStats.getMin(); y <= yStats.getMax(); y++) {
                if (!elves.contains(new Point(x, y))) {
                    emptyTiles++;
                }
            }
        }
        return emptyTiles;
    }

    private Set<Point> getElfNeighbours(Point elf) {
        Set<Point> neighbours = new HashSet<>();
        for (int x = elf.x - 1; x <= elf.x + 1; x++) {
            for (int y = elf.y - 1; y <= elf.y + 1; y++) {
                if (x != elf.x || y != elf.y) {
                    neighbours.add(Point.of(x, y));
                }
            }
        }
        return neighbours;
    }

    private Set<Point> readMap(String fileContents) {
        Set<Point> elves = new HashSet<>();
        String[] map = fileContents.split(System.lineSeparator()); // when input file is multiline
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length(); j++) {
                if ('#' == map[i].charAt(j)) {
                    elves.add(Point.of(i, j));
                }
            }
        }
        return elves;
    }

    public static void main(String[] args) {
        Day.run(Day23::new, "2022/D23_full.txt");
    }
}
