package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

public class Day15 extends Day {

    private static final Pattern FILE_PATTERN = Pattern.compile("Sensor at x=([0-9-]+)\\, y=([0-9-]+).*beacon is at x=([0-9-]+)\\, y=([0-9-]+)$");

    @Override
    protected String part1(String fileContents) throws Exception {
        List<Pair<Point, Point>> sensorsAndBeacons = readSensorsAndBeacons(fileContents);
        List<Point> allBeacons = sensorsAndBeacons.stream()
                .map(pair -> pair.b)
                .collect(Collectors.toList());

        int fixedY = getRowToCheckEmptyPositions(sensorsAndBeacons.size());

        Set<Point> totalEmptyPoints = new HashSet<>();
        for (Pair<Point, Point> sb : sensorsAndBeacons) {
            getAllEmptyPointsFor(sb.a, sb.b, fixedY)
                    .stream()
                    .filter(p -> !allBeacons.contains(p))
                    .forEach(totalEmptyPoints::add);
        }

        return String.valueOf(totalEmptyPoints.stream().filter(p -> p.y == fixedY).count());
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        List<Pair<Point, Point>> sensorsAndBeacons = readSensorsAndBeacons(fileContents);

        int coordsLimit = getMaxResultsLimit(sensorsAndBeacons.size());
        Point beacon;
        for (int x = 0; x <= coordsLimit; x++) {
            Optional<Integer> y = couldBeaconBeIn(sensorsAndBeacons, x);
            if (y.isPresent()) {
                beacon = new Point(x, y.get());
                return String.valueOf(getSignal(beacon));
            }
        }
        return StringUtils.EMPTY;
    }

    private List<Pair<Point, Point>> readSensorsAndBeacons(String fileContents){
        String[] sensors = fileContents.split(System.lineSeparator()); // when input file is multiline
        List<Pair<Point, Point>> sensorsAndBeacons = new ArrayList<>();

        for (String s : sensors) {
            Matcher matcher = FILE_PATTERN.matcher(s);
            if (matcher.find()) {
                sensorsAndBeacons.add(new Pair<>(
                        new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                        new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))));
            }
        }
        return sensorsAndBeacons;
    }

    private BigInteger getSignal(Point p) {
        return BigInteger.valueOf(p.getX()).multiply(BigInteger.valueOf(4000000)).add(BigInteger.valueOf(p.y));
    }

    private Optional<Integer> couldBeaconBeIn(List<Pair<Point, Point>> sensorsAndBeacons, int x) {

        int min = 0;
        int max = getMaxResultsLimit(sensorsAndBeacons.size());

        List<Range<Integer>> possibleRanges = new ArrayList<>(List.of(Range.between(min, max)));

        int distance, rowDistance;
        for (Pair<Point, Point> sb : sensorsAndBeacons) {
            distance = getManhattanDistance(sb.a, sb.b);

            rowDistance = Math.abs(x - sb.a.x);
            if (rowDistance <= distance) {
                possibleRanges = subtractRangeFrom(possibleRanges,
                        Range.between(
                                Math.max(min, sb.a.y - distance + rowDistance),
                                Math.min(max, sb.a.y + distance - rowDistance)));
            }

            if (possibleRanges.isEmpty()) {
                break;
            }
        }

        if (possibleRanges.size() == 1 && Objects.equals(possibleRanges.get(0).getMinimum(), possibleRanges.get(0).getMaximum())) {
            return Optional.of(possibleRanges.get(0).getMinimum());
        }
        return Optional.empty();
    }

    private List<Range<Integer>> subtractRangeFrom(List<Range<Integer>> currentRanges, Range<Integer> rangeToSubtract) {

        List<Range<Integer>> parsedRanges = new ArrayList<>();

        for (Range<Integer> r : currentRanges) {
            if (r.isOverlappedBy(rangeToSubtract)) {
                Range<Integer> intersection = r.intersectionWith(rangeToSubtract);
                if (!r.equals(intersection)) {
                    if (rangeToSubtract.getMinimum() > r.getMinimum()) {
                        parsedRanges.add(Range.between(r.getMinimum(), intersection.getMinimum() - 1));
                    }
                    if (rangeToSubtract.getMaximum() < r.getMaximum()){
                        parsedRanges.add(Range.between(intersection.getMaximum() + 1, r.getMaximum()));
                    }
                }                
            } else {
                parsedRanges.add(r);
            }
        }

        return parsedRanges;
    }

    private Set<Point> getAllEmptyPointsFor(Point sensor, Point beacon, int fixedYCoord) {
        int distance = getManhattanDistance(sensor, beacon);
        Set<Point> emptyPoints = new HashSet<>();

        Point other;
        for (int x = sensor.x - distance; x < sensor.x + distance; x++) {
            if (between(fixedYCoord, sensor.y - distance, sensor.y + distance)) {
                other = new Point(x, fixedYCoord);
                if (!other.equals(sensor)) {
                    if (getManhattanDistance(sensor, other) <= distance) {
                        emptyPoints.add(other);
                    }
                }
            }
        }
        return emptyPoints;
    }


    private int getManhattanDistance(Point p1, Point p2) {
        return getMaxAndMinDifference(p1.x, p2.x) + getMaxAndMinDifference(p1.y, p2.y);
    }

    private int getMaxAndMinDifference(int a, int b) {
        return a > b ? a - b : b - a;
    }

    private boolean between(int a, int min, int max) {
        return a >= min && a <= max;
    }

    private int getRowToCheckEmptyPositions(int numberOfSensors) {
        return numberOfSensors == 14 ? 10 : 2000000;
    }

    private int getMaxResultsLimit(int numberOfSensors) {
        return numberOfSensors == 14 ? 20 : 4000000;
    }

    public static void main(String[] args) {
        Day.run(Day15::new, "2022/D15_small.txt", "2022/D15_full.txt");
    }
}
