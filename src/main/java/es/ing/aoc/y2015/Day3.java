package es.ing.aoc.y2015;

import es.ing.aoc.common.Day;
import es.ing.aoc.common.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Day3 extends Day {

    private enum Direction {
        UP("^"),
        DOWN("v"),
        RIGHT(">"),
        LEFT("<");

        private final String directionCode;

        Direction(String code) {
            this.directionCode = code;
        }

        public static Direction of(String letter){
            for (Direction dir : Direction.values()){
                if (dir.directionCode.equalsIgnoreCase(letter)){
                    return dir;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected String part1(String fileContents) throws Exception {

        HashMap<Point, Integer> gifts = new HashMap<>();
        AtomicReference<Point> current = new AtomicReference<>(new Point(0, 0));
        gifts.put(current.get(), 1);

        Arrays.stream(fileContents.split("")).forEach(letter -> {

            current.set(getPointFrom(current.get(), Direction.of(letter)));

            if (gifts.containsKey(current.get())){
                gifts.put(current.get(), gifts.get(current.get()) + 1);
            } else {
                gifts.put(current.get(), 1);
            }
        });

        return String.valueOf(gifts.size());
    }

    @Override
    protected String part2(String fileContents) throws Exception {

        HashMap<Point, Integer> gifts = new HashMap<>();
        AtomicReference<Point> currentSanta = new AtomicReference<>(new Point(0, 0));
        AtomicReference<Point> currentRobot = new AtomicReference<>(new Point(0, 0));
        gifts.put(currentSanta.get(), 2);

        AtomicBoolean side = new AtomicBoolean(false);

        Arrays.stream(fileContents.split("")).forEach(letter -> {

            AtomicReference<Point> current;

            if (side.get()) {
                // Santa moves
                current = currentSanta;
            } else {
                // Robot moves
                current = currentRobot;
            }

            current.set(getPointFrom(current.get(), Direction.of(letter)));

            if (gifts.containsKey(current.get())) {
                gifts.put(current.get(), gifts.get(current.get()) + 1);
            } else {
                gifts.put(current.get(), 1);
            }


            side.set(!side.get());
        });

        return String.valueOf(gifts.size());
    }

    private Point getPointFrom(Point origin, Direction direction){

        Point newPoint = null;

        switch(direction){
            case UP:
                newPoint = new Point(origin.x - 1, origin.y);
                break;
            case DOWN:
                newPoint = new Point(origin.x + 1, origin.y);
                break;
            case LEFT:
                newPoint = new Point(origin.x, origin.y - 1);
                break;
            case RIGHT:
                newPoint = new Point(origin.x, origin.y + 1);
                break;
        }

        return newPoint;
    }

    public static void main(String[] args) {
        Day.run(Day3::new, "2015/D3_small.txt", "2015/D3_full.txt");
    }
}
