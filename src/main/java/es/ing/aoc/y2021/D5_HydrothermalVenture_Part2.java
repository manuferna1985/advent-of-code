package es.ing.aoc.y2021;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class D5_HydrothermalVenture_Part2 {

    private static class Vector {

        Point left;
        Point right;

        public static Vector of(Point left, Point right) {
            Vector v = new Vector();
            if (left.x > right.x) {
                v.left = right;
                v.right = left;
            } else {
                v.left = left;
                v.right = right;
            }
            return v;
        }

        public String toString() {
            return String.format("%s --> %s]", left, right);
        }
    }

    private static class Point {
        Integer x;
        Integer y;

        public static Point of(Integer x, Integer y) {
            Point p = new Point();
            p.x = x;
            p.y = y;
            return p;
        }

        public String toString() {
            return String.format("[%d,%d]", x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Objects.equals(x, point.x) && Objects.equals(y, point.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }


    public static void main(String[] args) throws Exception {

        // Initialize
        List<String> allLines = Files.readAllLines(
                Paths.get(D5_HydrothermalVenture_Part2.class.getResource("2021/D5_full.txt").toURI()), Charset.defaultCharset());

        List<Vector> vectors = new ArrayList<>();
        for (String line : allLines) {
            System.out.println(line);

            String[] p1 = line.substring(0, line.indexOf("-")).trim().split(",");
            String[] p2 = line.substring(line.indexOf(">") + 1).trim().split(",");

            vectors.add(Vector.of(
                    Point.of(Integer.parseInt(p1[0]), Integer.parseInt(p1[1])),
                    Point.of(Integer.parseInt(p2[0]), Integer.parseInt(p2[1]))));
        }

        System.out.println(vectors);

        Map<Point, Integer> impactMap = new HashMap<>();

        for (Vector v : vectors) {

            if (v.left.x.equals(v.right.x)) {
                // Move Y
                for (int i = Math.min(v.left.y, v.right.y); i <= Math.max(v.left.y, v.right.y); i++) {
                    fire(impactMap, Point.of(v.left.x, i));
                }
            } else if (v.left.y.equals(v.right.y)) {
                // Move X
                for (int i = Math.min(v.left.x, v.right.x); i <= Math.max(v.left.x, v.right.x); i++) {
                    fire(impactMap, Point.of(i, v.left.y));
                }
            } else {

                for (int i = Math.min(v.left.x, v.right.x), j = v.left.y; i <= Math.max(v.left.x, v.right.x); i++) {

                    fire(impactMap, Point.of(i, j));

                    if (v.left.y < v.right.y) {
                        j++;
                    } else {
                        j--;
                    }
                }
            }
        }

        System.out.println("Crosses: " + impactMap.values().stream().filter(impacts -> impacts > 1).count());

        System.out.println("end");
    }

    private static void fire(Map<Point, Integer> impactMap, Point p) {
        if (impactMap.containsKey(p)) {
            impactMap.put(p, impactMap.get(p) + 1);
        } else {
            impactMap.put(p, 1);
        }
    }
}
