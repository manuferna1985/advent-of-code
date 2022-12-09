package es.ing.aoc.y2022;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 extends Day {

    private static final IntUnaryOperator EQUAL = a -> a;
    private static final IntUnaryOperator MINUS = a -> a - 1;
    private static final IntUnaryOperator PLUS = a -> a + 1;

    public enum Direction {
        UP("U", PLUS, EQUAL),
        DOWN("D", MINUS, EQUAL),
        LEFT("L", EQUAL, MINUS),
        RIGHT("R", EQUAL, PLUS);

        private final String letter;
        private final IntUnaryOperator xFunc;
        private final IntUnaryOperator yFunc;

        Direction(String direction, IntUnaryOperator xFunc, IntUnaryOperator yFunc) {
            this.letter = direction;
            this.xFunc = xFunc;
            this.yFunc = yFunc;
        }

        public static Direction of(String letter) {
            return Arrays.stream(Direction.values())
                    .filter(d -> d.letter.equalsIgnoreCase(letter)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Unknown direction"));
        }
    }

    @Override
    protected void part1(String fileContents) throws Exception {
        String[] movements = fileContents.split(System.lineSeparator()); // when input file is multiline
        System.out.println("Part1: " + makeMovements(movements, 2));
    }

    @Override
    protected void part2(String fileContents) throws Exception {
        String[] movements = fileContents.split(System.lineSeparator()); // when input file is multiline
        System.out.println("Part2: " + makeMovements(movements, 10));
    }

    private int makeMovements(String[] movements, int numberOfKnots) {
        List<Point> knots = IntStream.rangeClosed(1, numberOfKnots)
                .mapToObj(i -> new Point(0, 0))
                .collect(Collectors.toList());

        final Point head = knots.get(0);
        final Point tail = knots.get(knots.size() - 1);

        Set<Point> visitedPositions = new HashSet<>();
        visitedPositions.add(new Point(tail));

        for (String mov : movements) {
            String[] parts = mov.split(" ");
            Direction dir = Direction.of(parts[0]);
            int movementLength = Integer.parseInt(parts[1]);

            for (int i = 0; i < movementLength; i++) {
                // First, lets calculate the new position for the head
                head.x = dir.xFunc.applyAsInt(head.x);
                head.y = dir.yFunc.applyAsInt(head.y);

                // Now, we should check if the rest of the knots need to be moved
                for (int k = 0; k < knots.size() - 1; k++) {
                    recalculateKnotPositions(knots.get(k), knots.get(k + 1));
                }

                visitedPositions.add(new Point(tail));
            }
        }
        return visitedPositions.size();
    }


    private void recalculateKnotPositions(Point knotBegin, Point knotEnd) {
        if (Math.abs(knotBegin.x - knotEnd.x) > 1 || Math.abs(knotBegin.y - knotEnd.y) > 1) {
            knotEnd.x = movementFunc(knotEnd.x, knotBegin.x);
            knotEnd.y = movementFunc(knotEnd.y, knotBegin.y);
        }
    }

    private int movementFunc(int a, int b){
        if (a == b || Math.abs(a - b) == 1) {
            return b;
        } else {
            return Math.min(a, b) + 1;
        }
    }

    public static void main(String[] args) {
        Day.run(Day9::new, "2022/D9_small.txt", "2022/D9_full.txt");
    }
}
