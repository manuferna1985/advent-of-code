package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import es.ing.aoc.common.Point;
import es.ing.aoc.common.RangeUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

public class Day15 extends Day {

    private static final Pattern FILE_PATTERN = Pattern.compile("Sensor at x=([0-9-]+)\\, y=([0-9-]+).*beacon is at x=([0-9-]+)\\, y=([0-9-]+)$");

    @Override
    protected String part1(String fileContents) throws Exception {
        List<Pair<Point, Point>> sensorsAndBeacons = readSensorsAndBeacons(fileContents);
        return String.valueOf(
                getPositionsWhereBeaconCouldntBe(
                        sensorsAndBeacons, getRowToCheckEmptyPositions(sensorsAndBeacons.size()))
                        .stream()
                        .mapToInt(rng -> rng.getMaximum() - rng.getMinimum() + 1)
                        .sum());
    }

    @Override
    protected String part2(String fileContents) throws Exception {
        List<Pair<Point, Point>> sensorsAndBeacons = readSensorsAndBeacons(fileContents);

        int coordsLimit = getMaxResultsLimit(sensorsAndBeacons.size());
        for (int x = 0; x <= coordsLimit; x++) {
            Optional<Integer> y = getPositionsWhereBeaconCouldBe(sensorsAndBeacons, x);
            if (y.isPresent()) {
                return String.valueOf(getSignal(y.get(), x));
            }
        }
        return StringUtils.EMPTY;
    }

    private List<Pair<Point, Point>> readSensorsAndBeacons(String fileContents) {
        String[] sensors = fileContents.split(System.lineSeparator()); // when input file is multiline
        List<Pair<Point, Point>> sensorsAndBeacons = new ArrayList<>();

        for (String s : sensors) {
            Matcher matcher = FILE_PATTERN.matcher(s);
            if (matcher.find()) {
                sensorsAndBeacons.add(new Pair<>(
                        new Point(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(1))),
                        new Point(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3)))));
            }
        }
        return sensorsAndBeacons;
    }

    private BigInteger getSignal(int x, int y) {
        return BigInteger.valueOf(x).multiply(BigInteger.valueOf(4000000)).add(BigInteger.valueOf(y));
    }

    private Optional<Integer> getPositionsWhereBeaconCouldBe(List<Pair<Point, Point>> sensorsAndBeacons, int x) {

        int min = 0;
        int max = getMaxResultsLimit(sensorsAndBeacons.size());

        Set<Range<Integer>> possibleRanges = new HashSet<>(Set.of(Range.between(min, max)));

        int distance, rowDistance;
        for (Pair<Point, Point> sb : sensorsAndBeacons) {
            distance = RangeUtils.getManhattanDistance(sb.a, sb.b);

            rowDistance = Math.abs(x - sb.a.x);
            if (rowDistance <= distance) {
                possibleRanges = RangeUtils.subtractRangeFrom(possibleRanges,
                        Range.between(
                                Math.max(min, sb.a.y - distance + rowDistance),
                                Math.min(max, sb.a.y + distance - rowDistance)));
            }

            if (possibleRanges.isEmpty()) {
                break;
            }
        }

        if (possibleRanges.size() == 1) {
            Range<Integer> first = possibleRanges.iterator().next();
            if (Objects.equals(first.getMinimum(), first.getMaximum())) {
                return Optional.of(first.getMinimum());
            }
        }
        return Optional.empty();
    }

    private Set<Range<Integer>> getPositionsWhereBeaconCouldntBe(List<Pair<Point, Point>> sensorsAndBeacons, int x) {
        Set<Range<Integer>> possibleRanges = new HashSet<>();

        int distance, rowDistance;
        for (Pair<Point, Point> sb : sensorsAndBeacons) {
            distance = RangeUtils.getManhattanDistance(sb.a, sb.b);

            rowDistance = Math.abs(x - sb.a.x);
            if (rowDistance <= distance) {
                possibleRanges = RangeUtils.addRangeTo(possibleRanges,
                        Range.between(
                                sb.a.y - distance + rowDistance,
                                sb.a.y + distance - rowDistance));
            }
        }

        // Substract current position of detected beacons, if they are in the inspected row
        for (Pair<Point, Point> sb : sensorsAndBeacons) {
            if (sb.b.getX() == x) {
                possibleRanges = RangeUtils.subtractRangeFrom(possibleRanges, Range.between(sb.b.getX(), sb.b.getX()));
            }
        }

        return possibleRanges;
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
