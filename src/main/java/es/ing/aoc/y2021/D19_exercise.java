package es.ing.aoc.y2021;

import es.ing.aoc.common.Point;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

public class D19_exercise {

    public static class Relation {

        Integer a;
        Integer b;

        Point difference;
        List<Point> aPoints;
        List<Point> bPoints;

        public Relation(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Relation relation = (Relation) o;
            return Objects.equals(a, relation.a) && Objects.equals(b, relation.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }

        public String toString() {
            return a + "--> " + b;
        }
    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D19_small.txt").toURI()), Charset.defaultCharset());

        // Start

        Map<Integer, List<Point>> points = new HashMap<>();
        List<Point> currentList = null;

        for (String line : allLines) {

            if (line.startsWith("---")) {
                currentList = new ArrayList<>();
                points.put(Integer.parseInt(line.substring(12, line.lastIndexOf("---") - 1)), currentList);
            } else if (line.contains(",")) {
                String[] strPoint = line.split(",");
                currentList.add(new Point(strPoint));
            }
        }

        System.out.println(points);

        Set<Integer> coveredPoints = new HashSet<>();
        coveredPoints.add(0);

        Point zero = new Point(0, 0, 0);

        List<Relation> relations = new ArrayList<>();

        boolean newRelations;
        do {
            newRelations = false;

            for (Integer p1 : points.keySet()) {
                for (Integer p2 : points.keySet()) {
                    Relation r = new Relation(p1, p2);
                    if (coveredPoints.contains(p1) && p2 > p1 && !relations.contains(r)) {
                        Point relative = calculateRelativePositionBetween(points, p1, p2);
                        if (relative != null) {
                            coveredPoints.add(p1);
                            coveredPoints.add(p2);
                            r.aPoints = points.get(p1);
                            r.bPoints = points.get(p2);
                            r.difference = relative;

                            newRelations = true;
                            relations.add(r);
                        }
                    }
                }
            }
        } while (newRelations);
    }

    private static Point calculateRelativePositionBetween(Map<Integer, List<Point>> points, Integer index1, Integer index2) {

        Point firstOverlap = null;
        Map<Integer, List<Point>> ownCombinations = generateCombinationsFrom(points.get(index1), index1);
        Map<Integer, List<Point>> otherCombinations = generateCombinationsFrom(points.get(index2), index2);

        // Alguna combinacion tiene que tener 12 overlaps

        for (Integer key1 : ownCombinations.keySet()) {

            for (Integer key2 : otherCombinations.keySet()) {

                Optional<Point> overlap = calculateOverlap(ownCombinations.get(key1), otherCombinations.get(key2));

                if (overlap.isPresent()) {
                    System.out.println("-------------------------------------------------------------------");
                    System.out.println("Overlap founded at: " + overlap.get());
                    System.out.println(String.format("%d(%s) --> %d(%s)", index1, key1, index2, key2));
                    System.out.println("-------------------------------------------------------------------");
    /*
                    // Loop over not common mines, adding them to allMines overall list
                    otherCombinations.get(key).forEach(point -> {
                        allMines.add(calculateSum(point, overlap.get()));
                    });
    */
                    //points.put(index2, otherCombinations.get(key));

                    if (firstOverlap == null){
                        firstOverlap = overlap.get();
                        return firstOverlap;
                    }
                }
            }
        }

        return firstOverlap;
    }

    private static Optional<Point> calculateOverlap(List<Point> points1, List<Point> points2) {

        Map<Point, Integer> differencesCounter = new HashMap<>();

        for (Point p1 : points1) {
            for (Point p2 : points2) {
                addDifferenceCount(differencesCounter, calculateDiff(p1, p2));
            }
        }

        Point mostRepeated = getMostRepeatedDiff(differencesCounter);
        Integer times = differencesCounter.get(mostRepeated);

        if (times >= 12) {
            return Optional.of(mostRepeated);
        } else {
            return Optional.empty();
        }
    }

    private static Point calculateDiff(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    private static Point calculateSum(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    private static void addDifferenceCount(Map<Point, Integer> counter, Point diff) {
        if (!counter.containsKey(diff)) {
            counter.put(diff, 0);
        }
        counter.put(diff, counter.get(diff) + 1);
    }

    private static Point getMostRepeatedDiff(Map<Point, Integer> counter) {
        return counter.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    private static Map<Integer, List<Point>> generateCombinationsFrom(List<Point> points, Integer pointId) {

        Integer[] increment = {100};
        Map<Integer, List<Point>> totalCombinations = new HashMap<>();

        List<UnaryOperator<Point>> functions = Arrays.asList(

                // 10X
                p -> new Point(p.x, p.y, p.z),
                // 20X
                p -> new Point(p.z, p.y, -p.x),
                // 30X
                p -> new Point(-p.x, p.y, -p.z),
                // 40X
                p -> new Point(-p.z, p.y, p.x),
                // 50X
                p -> new Point(p.x, -p.y, p.z),
                // 60X
                p -> new Point(p.z, -p.y, -p.x),
                // 70X
                p -> new Point(-p.x, -p.y, -p.z),
                // 80X
                p -> new Point(-p.z, -p.y, p.x),

                // 90X
                p -> new Point(-p.y, p.x, p.z),
                // 1000X
                p -> new Point(p.z, p.x, p.y),
                // 110X
                p -> new Point(p.y, p.x, -p.z),
                // 120X
                p -> new Point(-p.z, p.x, -p.y),
                // 1300X
                p -> new Point(-p.y, -p.x, p.z),
                // 1400X
                p -> new Point(p.z, -p.x, p.y),
                // 1500X
                p -> new Point(p.y, -p.x, -p.z),
                // 1600X
                p -> new Point(-p.z, -p.x, -p.y),

                // 1700X
                p -> new Point(p.x, p.z, -p.y),
                // 1800X
                p -> new Point(-p.y, p.z, -p.x),
                // 1900X
                p -> new Point(-p.x, p.z, p.y),
                // 2000X
                p -> new Point(p.y, p.z, p.x),
                // 2100X
                p -> new Point(p.x, -p.z, -p.y),
                // 2200X
                p -> new Point(-p.y, -p.z, -p.x),
                // 2300X
                p -> new Point(-p.x, -p.z, p.y),
                // 2400X
                p -> new Point(p.y, -p.z, p.x));

        functions.forEach(pointUnaryOperator -> {
            List<Point> combPoints = new ArrayList<>();
            points.forEach(point -> combPoints.add(pointUnaryOperator.apply(point)));
            for (Point p : points) {
                combPoints.add(new Point(p.x, p.y, p.z));
            }
            totalCombinations.put(getIncrement(increment) + pointId, combPoints);
        });

        return totalCombinations;
    }

    private static Integer getIncrement(Integer[] c) {
        Integer currentIncrement = c[0];
        c[0] += 100;
        return currentIncrement;
    }
}
