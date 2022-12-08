package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Pair;
import java.util.function.BiPredicate;

public class Day4 extends Day {

    private boolean isInsidePair(Integer point, Pair<Integer, Integer> pair){
        return (point >= pair.a && point <= pair.b);
    }

    private boolean isSubPairOf(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2){
        return p1.a >= p2.a && p1.b <= p2.b;
    }

    private boolean hasAnyEdgeInsidePair(Pair<Integer, Integer> pair1, Pair<Integer, Integer> pair2){
        return isInsidePair(pair1.a, pair2) || isInsidePair(pair1.b, pair2);
    }

    private boolean hasFullOverlap(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        return isSubPairOf(p1, p2) || isSubPairOf(p2, p1);
    }

    private boolean hasPartialOverlap(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        return hasAnyEdgeInsidePair(p2, p1) || hasAnyEdgeInsidePair(p1, p2);
    }

    private int countOverlaps(String[] packages, BiPredicate<Pair<Integer, Integer>, Pair<Integer, Integer>> function){
        int total = 0;
        for (String line : packages) {
            String[] pairs = line.split(",");
            Pair<Integer, Integer> p1 = Pair.of(pairs[0], "-");
            Pair<Integer, Integer> p2 = Pair.of(pairs[1], "-");
            if (function.test(p1, p2)){
                total++;
            }
        }
        return total;
    }


    @Override
    protected void part1(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline
        System.out.println("Part1: " + countOverlaps(packages, this::hasFullOverlap));
    }

    @Override
    protected void part2(String fileContents) throws Exception {
        String[] packages = fileContents.split(System.lineSeparator()); // when input file is multiline
        System.out.println("Part1: " + countOverlaps(packages, this::hasPartialOverlap));

    }

    public static void main(String[] args) {
        Day.run(Day4::new, "2022/D4_small.txt", "2022/D4_full.txt");
    }
}
