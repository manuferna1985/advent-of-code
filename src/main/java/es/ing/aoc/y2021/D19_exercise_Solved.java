package es.ing.aoc.y2021;

import es.ing.aoc.common.Day;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class D19_exercise_Solved extends Day {

    private static class Pair<A, B> {
        A a;
        B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public static <A, B> Pair<A, B> of(A a, B b) {
            return new Pair<>(a, b);
        }
    }

    private static class Point {
        int x;
        int y;
        int z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(Point one, Point two) {
            this(one.x + two.x, one.y + two.y, one.z + two.z);
        }

        public Point up(int up) {

            Point p;

            switch (up) {
                case 0:
                    p = this;
                    break;
                case 1:
                    p = new Point(x, -y, -z);
                    break;
                case 2:
                    p = new Point(x, -z, y);
                    break;
                case 3:
                    p = new Point(-y, -z, x);
                    break;
                case 4:
                    p = new Point(-x, -z, -y);
                    break;
                case 5:
                    p = new Point(y, -z, -x);
                    break;
                default:
                    p = null;
                    break;
            }

            return p;
        }

        public Point rot(int rot) {

            Point p;

            switch (rot) {
                case 0:
                    p = this;
                    break;
                case 1:
                    p = new Point(-y, x, z);
                    break;
                case 2:
                    p = new Point(-x, -y, z);
                    break;
                case 3:
                    p = new Point(y, -x, z);
                    break;
                default:
                    p = null;
                    break;
            }

            return p;
        }

        public int dist(Point other) {
            return Math.abs(other.x - x) + Math.abs(other.y - y) + Math.abs(other.z - z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point pos3 = (Point) o;
            return x == pos3.x && y == pos3.y && z == pos3.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class ScannerOrientation {
        final Scanner[] variations;

        public ScannerOrientation(Scanner sc) {
            variations = new Scanner[24];
            for (int up = 0; up < 6; up++) {
                for (int rot = 0; rot < 4; rot++) {
                    variations[rot + up * 4] = new Scanner(sc, up, rot);
                }
            }
        }

        public Scanner get(int up, int rot) {
            return variations[rot + up * 4];
        }
    }

    private static class Scanner {
        final List<Point> beacons;
        int[][] fingerprint;

        public Scanner() {
            this.beacons = new ArrayList<>();
        }

        public Scanner(Scanner other) {
            this.beacons = new ArrayList<>(other.beacons);
        }

        public Scanner(Scanner other, int up, int rot) {
            this.beacons = new ArrayList<>();
            for (var b : other.beacons) {
                this.beacons.add(b.up(up).rot(rot));
            }
        }

        public int[][] finger() {
            if (fingerprint == null) {
                fingerprint = new int[beacons.size()][beacons.size()];
                for (int i = 0; i < beacons.size(); i++) {
                    for (int j = 0; j < beacons.size(); j++) {
                        fingerprint[i][j] = beacons.get(i).dist(beacons.get(j));
                    }
                    Arrays.sort(fingerprint[i]);
                }
            }
            return fingerprint;
        }

        public boolean fingerMatch(Scanner other) {
            for (int i = 0; i < beacons.size(); i++) {
                for (int j = 0; j < other.beacons.size(); j++) {
                    var p1 = finger()[i];
                    var p2 = other.finger()[j];
                    // check if fingerprint matches
                    int x = 0;
                    int y = 0;
                    int count = 0;
                    while (x < p1.length && y < p2.length) {
                        if (p1[x] == p2[y]) {
                            x++;
                            y++;
                            count++;
                            if (count >= 12) return true;
                        } else if (p1[x] > p2[y]) {
                            y++;
                        } else if (p1[x] < p2[y]) {
                            x++;
                        }
                    }
                }
            }
            return false;
        }

        private Point test(Scanner other) {
            for (int i = 0; i < beacons.size(); i++) {
                for (int j = 0; j < other.beacons.size(); j++) {
                    var mine = beacons.get(i);
                    var their = other.beacons.get(j);
                    var relx = their.x - mine.x;
                    var rely = their.y - mine.y;
                    var relz = their.z - mine.z;
                    int count = 0;
                    for (int k = 0; k < beacons.size(); k++) {
                        if ((count + beacons.size() - k) < 12) break; // not possible
                        for (int l = 0; l < other.beacons.size(); l++) {
                            var m = beacons.get(k);
                            var n = other.beacons.get(l);
                            if ((relx + m.x) == n.x && (rely + m.y) == n.y && (relz + m.z) == n.z) {
                                count++;
                                if (count >= 12) return new Point(relx, rely, relz);
                                break;
                            }
                        }
                    }
                }
            }
            return null;
        }

        /**
         * Try to match the given scanner (for all orientations) with our beacons
         */
        public Pair<Scanner, Point> match(ScannerOrientation other) {
            for (int i = 0; i < other.variations.length; i++) {
                var sc = other.variations[i];
                var mat = test(sc);
                if (mat != null) return Pair.of(sc, mat);
            }
            return null;
        }

        /**
         * Add all unique beacons from the other scanner (with relative position) to our beacon list
         */
        public void add(Scanner other, Point relPos) {
            for (int l = 0; l < other.beacons.size(); l++) {
                var n = other.beacons.get(l);
                n = new Point(n.x - relPos.x, n.y - relPos.y, n.z - relPos.z);
                if (!beacons.contains(n)) beacons.add(n);
            }
        }
    }

    /**
     * Parse the given input into a list of scanner
     */
    private List<Scanner> parse(String fileContents) {
        List<Scanner> scanners = new ArrayList<>();
        Scanner curSc = new Scanner();

        for (var line : fileContents.split("[\r\n]+")) {
            if (line.startsWith("---")) {
                // new scanner
                assert Integer.parseInt(line.split(" ")[2]) == scanners.size();
                curSc = new Scanner();
                scanners.add(curSc);
            } else {
                var a = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                curSc.beacons.add(new Point(a[0], a[1], a[2]));
            }
        }

        return scanners;
    }

    /**
     * Given a list of scanners, produce a list of scanner-orientations
     */
    private List<ScannerOrientation> orientations(List<Scanner> scanners) {
        List<ScannerOrientation> result = new ArrayList<>();
        for (var s : scanners) result.add(new ScannerOrientation(s));
        return result;
    }

    @Override
    protected void part1(String fileContents) {
        var scanners = orientations(parse(fileContents)).toArray(ScannerOrientation[]::new);
        var orientation = new Scanner[scanners.length];
        var position = new Point[scanners.length];

        orientation[0] = scanners[0].get(0, 0);
        position[0] = new Point(0, 0, 0);

        Queue<Integer> frontier = new ArrayDeque<>();
        frontier.add(0);

        while (!frontier.isEmpty()) {
            var front = frontier.poll();
            for (int i = 0; i < scanners.length; i++) {
                if (position[i] == null) {
                    if (scanners[front].get(0, 0).fingerMatch(scanners[i].get(0, 0))) {
                        var match = orientation[front].match(scanners[i]);
                        if (match != null) {
                            orientation[i] = match.a; // correct orientation!
                            position[i] = new Point(position[front], match.b);
                            frontier.add(i);
                        }
                    }
                }
            }
        }

        var result = new Scanner(orientation[0]);
        for (int i = 1; i < scanners.length; i++) {
            result.add(orientation[i], position[i]);
        }

        int maxDist = Integer.MIN_VALUE;
        for (int i = 0; i < position.length; i++) {
            for (int j = 0; j < position.length; j++) {
                var one = position[i];
                var two = position[j];
                var d = Math.abs(one.x - two.x) + Math.abs(one.y - two.y) + Math.abs(one.z - two.z);
                maxDist = Math.max(maxDist, d);
            }
        }

        System.out.println("Part 1: " + result.beacons.size());
        System.out.println("Part 2: " + maxDist);
    }

    @Override
    protected void part2(String fileContents) {
        // Actually we did part 2 in part 1...
    }

    public static void main(String[] args) {
        Day.run(D19_exercise_Solved::new, "2021/D19_small.txt", "2021/D19_full.txt");
    }
}