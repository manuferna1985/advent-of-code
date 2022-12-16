package es.ing.aoc.common;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Range;

public class RangeUtils {

    private RangeUtils() {
        throw new RuntimeException("Constructor not meant to be called");
    }

    public static Set<Range<Integer>> addRangeTo(Set<Range<Integer>> currentRanges, Range<Integer> rangeToAdd) {
        Set<Range<Integer>> parsedRanges = new HashSet<>(subtractRangeFrom(currentRanges, rangeToAdd));
        parsedRanges.add(rangeToAdd);
        return parsedRanges;
    }

    public static Set<Range<Integer>> subtractRangeFrom(Set<Range<Integer>> currentRanges, Range<Integer> rangeToSubtract) {
        Set<Range<Integer>> parsedRanges = new HashSet<>();
        for (Range<Integer> r : currentRanges) {
            if (r.isOverlappedBy(rangeToSubtract)) {
                Range<Integer> intersection = r.intersectionWith(rangeToSubtract);
                if (!r.equals(intersection)) {
                    if (rangeToSubtract.getMinimum() > r.getMinimum()) {
                        parsedRanges.add(Range.between(r.getMinimum(), intersection.getMinimum() - 1));
                    }
                    if (rangeToSubtract.getMaximum() < r.getMaximum()) {
                        parsedRanges.add(Range.between(intersection.getMaximum() + 1, r.getMaximum()));
                    }
                }
            } else {
                parsedRanges.add(r);
            }
        }
        return parsedRanges;
    }

    public static int getManhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}
