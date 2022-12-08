package es.ing.aoc.y2021;

import es.ing.aoc.common.Point;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D23_exercise {

    public enum Type {
        A, B, C, D;

        public static Type of(String letter) {

            for (Type t : Type.values()) {
                if (t.name().equalsIgnoreCase(letter)) {
                    return t;
                }
            }

            throw new IllegalArgumentException("Unknown type: " + letter);
        }

        public String toString() {
            return this.name();
        }

        public int getCost() {
            int cost = 0;
            switch (this) {
                case A:
                    cost = 1;
                    break;
                case B:
                    cost = 10;
                    break;
                case C:
                    cost = 100;
                    break;
                case D:
                    cost = 1000;
                    break;
            }
            return cost;
        }
    }

    public interface Thing {

    }

    public static class Space implements Thing {
        public String toString() {
            return " ";
        }
    }

    public static class Wall implements Thing {

        public String toString() {
            return "#";
        }

    }

    public static class Amphipod implements Thing {

        Type type;
        Point position;

        private Amphipod(Type type, Point position) {
            this.type = type;
            this.position = position;
        }

        public static Amphipod of(Type type, Point position) {
            return new Amphipod(type, position);
        }

        public String toString() {
            return type.toString();
        }
    }

    private static void printMatrix(Thing[][] cave) {

        System.out.println("-------------------------------------------");

        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                System.out.print(cave[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------------------------------------");

    }

    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D19_exercise.class.getResource("2021/D23_full.txt").toURI()), Charset.defaultCharset());

        // Start coding here. ;-)
        long before = System.nanoTime();

        Thing[][] cave = new Thing[allLines.size()][allLines.get(0).length()];
        List<Amphipod> amphipods = new ArrayList<>();

        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[i].length; j++) {
                cave[i][j] = new Wall();
            }
        }

        int i = 0;
        for (String line : allLines) {
            int j = 0;
            for (String letter : line.split("")) {

                switch (letter) {
                    case "#":
                    case " ":
                        cave[i][j] = new Wall();
                        break;
                    case ".":
                        cave[i][j] = new Space();
                        break;
                    default:
                        Amphipod newAmphipod = Amphipod.of(Type.of(letter), new Point(i, j));
                        cave[i][j] = newAmphipod;
                        amphipods.add(newAmphipod);
                        break;
                }
                j++;
            }
            i++;
        }

        final Map<Type, List<Point>> targetRooms = new HashMap<>();
        targetRooms.put(Type.A, List.of(new Point(2, 3), new Point(3, 3)));
        targetRooms.put(Type.B, List.of(new Point(2, 5), new Point(3, 5)));
        targetRooms.put(Type.C, List.of(new Point(2, 7), new Point(3, 7)));
        targetRooms.put(Type.D, List.of(new Point(2, 9), new Point(3, 9)));

        final List<Point> allowedEndPositions = new ArrayList<>();
        allowedEndPositions.add(new Point(1, 1));
        allowedEndPositions.add(new Point(1, 2));
        allowedEndPositions.add(new Point(1, 4));
        allowedEndPositions.add(new Point(1, 5));
        allowedEndPositions.add(new Point(1, 8));
        allowedEndPositions.add(new Point(1, 10));
        allowedEndPositions.add(new Point(1, 11));

        Integer[] costs = new Integer[]{0, Integer.MAX_VALUE};
        orderAmphipods(cave, amphipods, targetRooms, allowedEndPositions, costs);

        long after = System.nanoTime();

        System.out.printf("Elapsed time: %.0f ms%n%n", ((double) (after - before)) / 1000000);
    }

    private static void orderAmphipods(Thing[][] cave, List<Amphipod> amphipods, Map<Type, List<Point>> targetRooms, List<Point> allowedEndPositions, Integer[] costs) {

        //printMatrix(cave);
        if (costs[0] > costs[1]) {
            // Don't continue exploring this path
            return;
        }

        // Final condition
        if (!caveOrdered(amphipods, targetRooms)) {

            // Calculate amphipods which could move
            amphipods.forEach(amp -> {

                if (amp.position.x == 1) {
                    // Amphipod in the hallway. Calculate possible destinations (common sense).
                    final List<Point> specificEndPositionsForMe = new ArrayList<>(targetRooms.get(amp.type));

                    specificEndPositionsForMe.stream()
                            .filter(end -> cave[end.x][end.y] instanceof Space)
                            .filter(end -> cave[end.x + 1][end.y] instanceof Wall || sameAmphiPodOn(amp.type, cave[end.x + 1][end.y]))
                            .filter(end -> isPathClear(cave, amp.position, end)).forEach(end -> {
                                int moves = getMoves(amp.position, end);

                                Point rollbackPos = amp.position;
                                Integer movementCost = amp.type.getCost() * moves;

                                moveAmphipod(cave, amp, end);
                                costs[0] += movementCost;

                                orderAmphipods(cave, amphipods, targetRooms, allowedEndPositions, costs);

                                moveAmphipod(cave, amp, rollbackPos);
                                costs[0] -= movementCost;
                            });

                } else if (amp.position.x == 2) {
                    // Amphipod in the first cave position. Should move?

                    boolean movementAllowed = true;

                    if (targetRooms.get(amp.type).contains(amp.position)) {
                        // Is well positioned, unless X3 is not
                        movementAllowed = false;
                        if (cave[3][amp.position.y] instanceof Amphipod) {
                            Amphipod bottom = (Amphipod) cave[3][amp.position.y];
                            movementAllowed = !bottom.type.equals(amp.type);
                        }
                    }

                    if (movementAllowed) {

                        allowedEndPositions.stream()
                                .filter(end -> cave[end.x][end.y] instanceof Space)
                                .filter(end -> isPathClear(cave, amp.position, end)).forEach(end -> {
                                    int moves = getMoves(amp.position, end);
                                    Point rollbackPos = amp.position;
                                    Integer movementCost = amp.type.getCost() * moves;

                                    moveAmphipod(cave, amp, end);
                                    costs[0] += movementCost;

                                    orderAmphipods(cave, amphipods, targetRooms, allowedEndPositions, costs);

                                    moveAmphipod(cave, amp, rollbackPos);
                                    costs[0] -= movementCost;

                                });
                    }
                } else if (amp.position.x == 3) {
                    // Amphipod in the back cave position. Should & can move?
                    if (!targetRooms.get(amp.type).contains(amp.position)) {
                        // Not target room, if it can move, it moves
                        // UP
                        if (cave[amp.position.x - 1][amp.position.y] instanceof Space) {
                            Point rollbackPos = amp.position;
                            Integer movementCost = amp.type.getCost();

                            moveAmphipod(cave, amp, new Point(amp.position.x - 1, amp.position.y));
                            costs[0] += movementCost;

                            orderAmphipods(cave, amphipods, targetRooms, allowedEndPositions, costs);

                            moveAmphipod(cave, amp, rollbackPos);
                            costs[0] -= movementCost;
                        }
                    }
                } else {
                    throw new IllegalArgumentException("This is possible???");
                }
            });
        } else {
            Integer newMin = Math.min(costs[1], costs[0]);
            if (!newMin.equals(costs[1])) {
                System.out.println("Finished. Min: " + costs[1]);
            }
            costs[1] = newMin;
        }
    }

    private static boolean sameAmphiPodOn(Type expectedType, Thing thing) {
        if (thing instanceof Amphipod) {
            Amphipod other = (Amphipod) thing;
            return other.type.equals(expectedType);
        }
        return false;
    }

    private static boolean caveOrdered
            (List<Amphipod> amphipods, Map<Type, List<Point>> targetRooms) {
        return amphipods.stream().allMatch(amphipod -> targetRooms.get(amphipod.type).contains(amphipod.position));
    }

    private static void moveAmphipod(Thing[][] cave, Amphipod amphipod, Point dest) {
        cave[amphipod.position.x][amphipod.position.y] = new Space();
        amphipod.position = dest;
        cave[dest.x][dest.y] = amphipod;
    }

    private static boolean isPathClear(Thing[][] cave, Point origin, Point destination) {

        if (origin.x == 2 && destination.x == 1) {

            // #############
            // #FF.F.F.F.FF#    1
            // ###I#I#I#I###    2
            //   #.#.#.#.#
            //   #########

            List<Point> points = new ArrayList<>();
            for (int j = origin.y; j <= destination.y; j++) {
                points.add(new Point(1, j));
            }

            return points.stream().allMatch(p -> cave[p.x][p.y] instanceof Space);

        } else if (origin.x == 1) {
            // #############
            // #II.I.I.I.II#    1
            // ###F#F#F#F###    2
            //   #F#F#F#F#      3
            //   #########

            List<Point> points = new ArrayList<>();

            points.add(new Point(2, destination.y));
            if (destination.x == 3) {
                points.add(new Point(3, destination.y));
            }

            for (int j = Math.min(origin.y, destination.y); j <= Math.max(origin.y, destination.y); j++) {
                points.add(new Point(1, j));
            }
            points.remove(origin);

            return points.stream().allMatch(p -> cave[p.x][p.y] instanceof Space);
        }

        return false;
    }

    private static int getMoves(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}
